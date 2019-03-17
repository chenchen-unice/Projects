#!/usr/bin/env python

'''
Camshift tracker
================

This is a demo that shows mean-shift based tracking
You select a color objects such as your face and it tracks it.
This reads from video camera (0 by default, or the camera number the user enters)

http://www.robinhewitt.com/research/track/camshift.html

Usage:
------
    camshift.py [<video source>]

    To initialize tracking, select the object with mouse

Keys:
-----
    ESC   - exit
    b     - toggle back-projected probability visualization
'''

# Python 2/3 compatibility
from __future__ import print_function
import sys
PY3 = sys.version_info[0] == 3

if PY3:
    xrange = range

import numpy as np
import cv2 as cv

# local module
import video
from video import presets
from video import create_capture
from common import clock, draw_str

def detect(img, cascade):
    ##function cascade to find a face
    rects = cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(30, 30),
                                     flags=cv.CASCADE_SCALE_IMAGE)
    if len(rects) == 0:
        return []
    ## store the data of rect with an array
    rects[:,2:] += rects[:,:2]
    return rects

def draw_rects(img, rects, color):
    for x1, y1, x2, y2 in rects:
        ## function rectangle in opencv to draw a rect on the screen
        cv.rectangle(img, (x1, y1), (x2, y2), color, 2)

class App(object):
    def __init__(self, video_src):
        self.cam = video.create_capture(video_src, presets['cube'])
        _ret, self.frame = self.cam.read()
        cv.namedWindow('camshift')
        # cv.setMouseCallback('camshift', self.onmouse)
        args, video_src = getopt.getopt(sys.argv[1:], '', ['cascade=', 'nested-cascade='])
        try:
            video_src = video_src[0]
        except:
            video_src = 0
        args = dict(args)
        cascade_fn = args.get('--cascade', "data/haarcascades/haarcascade_frontalface_alt.xml")
        nested_fn = args.get('--nested-cascade', "data/haarcascades/haarcascade_eye.xml")

        self.cascade = cv.CascadeClassifier(cv.samples.findFile(cascade_fn))
        self.nested = cv.CascadeClassifier(cv.samples.findFile(nested_fn))

        self.cam = create_capture(video_src,
                             fallback='synth:bg={}:noise=0.05'.format(cv.samples.findFile('samples/data/lena.jpg')))

        self.selection = None
        # self.drag_start = None
        self.show_backproj = False
        self.track_window = None

    # def onmouse(self, event, x, y, flags, param):
    #     if event == cv.EVENT_LBUTTONDOWN:
    #         self.drag_start = (x, y)
    #         self.track_window = None
    #     if self.drag_start:
    #         xmin = min(x, self.drag_start[0])
    #         ymin = min(y, self.drag_start[1])
    #         xmax = max(x, self.drag_start[0])
    #         ymax = max(y, self.drag_start[1])
    #         self.selection = (xmin, ymin, xmax, ymax)
    #     if event == cv.EVENT_LBUTTONUP:
    #         self.drag_start = None
    #         self.track_window = (xmin, ymin, xmax - xmin, ymax - ymin)


    def show_hist(self):
        bin_count = self.hist.shape[0]
        bin_w = 24
        img = np.zeros((256, bin_count*bin_w, 3), np.uint8)
        for i in xrange(bin_count):
            h = int(self.hist[i])
            cv.rectangle(img, (i*bin_w+2, 255), ((i+1)*bin_w-2, 255-h), (int(180.0*i/bin_count), 255, 255), -1)
        img = cv.cvtColor(img, cv.COLOR_HSV2BGR)
        cv.imshow('hist', img)

    def run(self):
        Redrect = np.array([0, 0, 0, 0])  ## the array to store the red rectangle
        total_time = 0.0  ## total time which we cant find a face
        while True:
            _ret, self.frame = self.cam.read()
            gray = cv.cvtColor(self.frame, cv.COLOR_BGR2GRAY)  ## turn the frame from RPG to GRAY
            gray = cv.equalizeHist(gray)  ##optimize the frame
            vis = self.frame.copy()
            hsv = cv.cvtColor(self.frame, cv.COLOR_BGR2HSV)
            mask = cv.inRange(hsv, np.array((0., 60., 32.)), np.array((180., 255., 255.)))
            #Checks if array elements lie between the elements of two other arrays

            t = clock()
            find_time = 0.0  ## the time we use to find a face in each frame, if this arg = 0, it means that we cant find a face in frame

            # if we haven't a coordinate of the redrect.or we cant find a face in 2000ms,
            # we scan the whole frame first
            if Redrect.all() == 0 or total_time > 2000:
                rects = detect(gray, self.cascade)  ## detect the faces from the whole frame
                if len(rects):
                    Redrect = rects[0]
                    # if we find the faces, we update the data of the Redrect,
                    # and the area of the Redrect is 100 pixel bigger than the rect of the face
                    Redrect = np.array([Redrect[0] - 100, Redrect[1] - 100, Redrect[2] + 100, Redrect[3] + 100])
                    total_time = 0.0  # initialize the total_time,cause we find a face
            else:
                Red_gray = gray[Redrect[1]: Redrect[3], Redrect[0]: Redrect[2]]
                Red_vis = vis[Redrect[1]: Redrect[3], Redrect[0]: Redrect[2]]
                sub_rects = detect(Red_gray.copy(), self.cascade)
                cv.rectangle(vis, (Redrect[0], Redrect[1]), (Redrect[2], Redrect[3]), (0, 0, 255),
                             2)
                if len(sub_rects):
                    find_time = (clock() - t) * 1000  # we update the frame_time only when we find the face
                    draw_rects(Red_vis, sub_rects, (0, 255, 0))
                    total_time = 0.0  # initialize the total time

            if not self.nested.empty():
                for x1, y1, x2, y2 in rects:
                    roii = gray[y1:y2, x1:x2]
                    vis_roii = vis[y1:y2, x1:x2]
                    subrects = detect(roii.copy(), self.nested)
                    draw_rects(vis_roii, subrects, (255, 0, 0))

            dt = clock() - t  # the time we use to iterate a loop
            if find_time == 0.0:  # it means that we cant find a face
                total_time += dt * 1000  # update the total time we cant find a face

            draw_str(vis, (20, 20), 'time: %.1f ms' % (dt * 1000))  ## show the time we use in each frame


    #####################################
            if len(rects):
              Location = np.array([0, 0, 0, 0])
              Location = rects[0]
            # Location = np.array([Location[0], Location[1], Location[2], Location[3]])

              xmin = Location[0]
              ymin = Location[1]
              xmax = Location[2]
              ymax = Location[3]
              self.selection = (xmin, ymin, xmax, ymax)
              self.track_window = (xmin, ymin, xmax - xmin, ymax - ymin)


            if self.selection:
                x0, y0, x1, y1 = self.selection
                hsv_roi = hsv[y0:y1, x0:x1]
                mask_roi = mask[y0:y1, x0:x1]
                hist = cv.calcHist( [hsv_roi], [0], mask_roi, [16], [0, 180] )
                cv.normalize(hist, hist, 0, 255, cv.NORM_MINMAX)
                self.hist = hist.reshape(-1)
                self.show_hist()

                vis_roi = vis[y0:y1, x0:x1]
                cv.bitwise_not(vis_roi, vis_roi)
                vis[mask == 0] = 0

            if self.track_window and self.track_window[2] > 0 and self.track_window[3] > 0:
                self.selection = None
                prob = cv.calcBackProject([hsv], [0], self.hist, [0, 180], 1)
                prob &= mask
                prob[y0:y1, x0:x1] = 0
                term_crit = ( cv.TERM_CRITERIA_EPS | cv.TERM_CRITERIA_COUNT, 10, 1 )
                track_box, self.track_window = cv.CamShift(prob, self.track_window, term_crit)

                if self.show_backproj:
                    vis[:] = prob[...,np.newaxis]
                try:
                    cv.ellipse(vis, track_box, (0, 0, 255), 2)
                except:
                    print(track_box)

            cv.imshow('camshift', vis)

            ch = cv.waitKey(5)
            if ch == 27:
                break
            if ch == ord('b'):
                self.show_backproj = not self.show_backproj
        cv.destroyAllWindows()


if __name__ == '__main__':
    import sys, getopt
    try:
        video_src = sys.argv[1]
    except:
        video_src = 0
    print(__doc__)
    App(video_src).run()
