function [ xm,fv ] = test( f,N,c1,c2,w,Step,D )
% f,Test_functions
% Nombre de particules£ºN
% tir¨¦ al¨¦atoirement1£ºCI
% tir¨¦ al¨¦atoirement2£ºCg
% inertie£ºw
% Coefficient d'it¨¦ration maximum£ºStep
% Le nombre de variables£ºD
% La valeur de variables quand Test_function soit minimal£ºxm
% La valeur minimal de la Test_function£ºfv
vmax=0.2*2*pi;
vmin=-0.2*2*pi;
xmax=512;
xmin=-512;

for i=1:N
    for j=1:D
        
         x(i,j)=rand(1)*1000;
         v(i,j)=rand(1)*1000;

    end    
end
     x(x > xmax) = xmax;  
     x(x < xmin) = xmin;  
     v(v > vmax) = vmax;  
     v(v < vmin) = vmin;  
 for i=1:N
     p(i)=f(x(i,:));
     y(i,:)=x(i,:);
 end
 pg=x(N,:);
 for i=1:N-1
     if f(x(i,:)) < f(pg)
         pg=x(i,:);
     end
 end
for t=1:Step
    for i=1:N
        v(i,:)=w*v(i,:)+c1*rand*(y(i,:)-x(i,:))+c2*rand*(pg-x(i,:));
        x(i,:)=x(i,:)+v(i,:);
          if f(x(i,:)) < p(i)
              p(i)=f(x(i,:));
              y(i,:)=x(i,:);
          end
 if p(i) < f(pg)
            pg=y(i,:);
        end
    end
%      disp(x);
%      disp(f(pg));
%           
%      for i=1:N
%          axis ([0 600 0 600]);
%          plot(x(i),x(i+N),"x");
%          hold on
%      end
%      pause(0.1)
%      hold off
%      
end
xm=pg;
fv=f(pg);
end