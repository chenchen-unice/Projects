#!/usr/bin/env python

'''
face detection using haar cascades

USAGE:
    facedetect.py [--cascade <cascade_fn>] [--nested-cascade <cascade_fn>] [<video_source>]
'''

# Python 2/3 compatibility
from __future__ import print_function

import numpy as np
import cv2 as cv


# local modules
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

#
# def draw_Redrects(img,rects,color):
#         for x1, y1, x2, y2 in rects:
#             cv.rectangle(img, (x1 - 100, y1 - 100), (x2 + 100, y2 + 100), color, 2, )

if __name__ == '__main__':
    Redrect = np.array([0,0,0,0]) ## the array to store the red rectangle
    total_time = 0.0  ## total time which we cant find a face

    import sys, getopt
    print(__doc__)

    args, video_src = getopt.getopt(sys.argv[1:], '', ['cascade=', 'nested-cascade='])
    try:
        video_src = video_src[0]
    except:
        video_src = 0
    args = dict(args)
    cascade_fn = args.get('--cascade', "data/haarcascades/haarcascade_frontalface_alt.xml")
    nested_fn  = args.get('--nested-cascade', "data/haarcascades/haarcascade_eye.xml")

    cascade = cv.CascadeClassifier(cv.samples.findFile(cascade_fn))
    nested = cv.CascadeClassifier(cv.samples.findFile(nested_fn))

    cam = create_capture(video_src, fallback='synth:bg={}:noise=0.05'.format(cv.samples.findFile('samples/data/lena.jpg')))

    while True:

        ret, img = cam.read()  ##read the frame with the camera
        gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)  ## turn the frame from RPG to GRAY
        gray = cv.equalizeHist(gray) ##optimize the frame
        vis = img.copy() ## make a copy of the frame

        t = clock()
        find_time = 0.0  ## the time we use to find a face in each frame, if this arg = 0, it means that we cant find a face in frame

        # if we haven't a coordinate of the redrect.or we cant find a face in 2000ms,
        # we scan the whole frame first
        if Redrect.all() == 0 or total_time > 2000:
          rects = detect(gray, cascade) ## detect the faces from the whole frame
          if len(rects):
            Redrect = rects[0]
            # if we find the faces, we update the data of the Redrect,
            # and the area of the Redrect is 100 pixel bigger than the rect of the face
            Redrect = np.array([Redrect[0]-100, Redrect[1]-100, Redrect[2]+100, Redrect[3]+100])
            total_time = 0.0 #initialize the total_time,cause we find a face
        else:
          Red_gray = gray[Redrect[1]: Redrect[3], Redrect[0]: Redrect[2]]
          Red_vis = vis[Redrect[1]: Redrect[3], Redrect[0]: Redrect[2]]
          sub_rects = detect(Red_gray.copy(), cascade)
          cv.rectangle(vis, (Redrect[0], Redrect[1]), (Redrect[2], Redrect[3]), (0, 0, 255),
                       2)
          if len(sub_rects) :
                     find_time = (clock() - t) * 1000 # we update the frame_time only when we find the face
                     draw_rects(Red_vis, sub_rects, (0, 255, 0))
                     total_time = 0.0 #initialize the total time



        if not nested.empty():
            for x1, y1, x2, y2 in rects:
                roi = gray[y1:y2, x1:x2]
                vis_roi = vis[y1:y2, x1:x2]
                subrects = detect(roi.copy(), nested)
                draw_rects(vis_roi, subrects, (255, 0, 0))

        dt = clock() - t # the time we use to iterate a loop
        if find_time == 0.0 : # it means that we cant find a face
            total_time += dt * 1000 # update the total time we cant find a face


        draw_str(vis, (20, 20), 'time: %.1f ms' % (dt*1000)) ## show the time we use in each frame
        cv.imshow('facedetect', vis)

        if cv.waitKey(5) == 27:
            break
    cv.destroyAllWindows()
