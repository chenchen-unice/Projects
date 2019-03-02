//Initialisation
A=0;
B=1;
M=50;
N=25;
x0=0.25;
segma=0.1;
m=7;
L=1;
c=1; 
//Discrétisation spatiale
deltaX = 1/M;
// Discrétisation temporelle
deltaT=0.9*deltaX/c;



xx=linspace(A,B,M+1) // HABBAL
uDecentre=zeros(M+1,N+1);
uLaxFriedreich=zeros(M+1,N+1);
uLaxWendroff=zeros(M+1,N+1);

uExact=zeros(M+1,N+1);

function y=Uini(x)
     //y = exp(-(x - x0)**2/segma**2)
     if x < x0 then
        y = 1;
     else
        y = 0;
     end
//    y=sin(m*%pi*x)+sin((m*%pi*x)/3)
endfunction

function y=Uex(x,t)
    y = Uini(x-c*t)
endfunction

function y=a(t)
    y=Uex(0,t)
endfunction

// HABBAL
tnn=0.;
for nn=1:N
    for i=1:M+1
      uExact(i,nn)=Uex(xx(i),tnn);
     
  end;
   tnn=tnn+deltaT ;  
end
// HABBAL

function y=f(x,t)
    y=0
endfunction

for i=1:M+1
    uDecentre(i,1) = Uini(xx(i));
    uLaxFriedreich(i,1) = Uini(xx(i));
    uLaxWendroff(i,1) = Uini(xx(i));
end

tn=0

for n=1:N
    
    
    uDecentre(1,n)=a(tn)
    uLaxFriedreich(1,n)=a(tn)
    uLaxWendroff(1,n)=a(tn);
        
    //On implémente les 3 schémas
 
    for j=2:M
        //Schéma décentré
        uDecentre(j,n+1) = -c*(uDecentre(j,n)-uDecentre(j-1,n))*deltaT/deltaX + uDecentre(j,n)
        //Schéma Lax-Friedrichs
        uLaxFriedreich(j,n+1) = -c*(deltaT/deltaX)*0.5*(uLaxFriedreich(j+1,n)-uLaxFriedreich(j-1,n)) + 0.5*(uLaxFriedreich(j-1,n)+uLaxFriedreich(j+1,n))
        //Schéma Lax-Wendroff
        uLaxWendroff(j,n+1) = uLaxWendroff(j,n) - c*deltaT*(uLaxWendroff(j+1,n)-uLaxWendroff(j-1,n))/(2*deltaX)+ 0.5*(c^2)*(deltaT^2)*(uLaxWendroff(j+1,n)-2*uLaxWendroff(j,n)+uLaxWendroff(j-1,n))/deltaX^2 
    end
    
    uDecentre(M+1,n+1) = -c*(uDecentre(M+1,n)-uDecentre(M,n))*deltaT/deltaX + uDecentre(M+1,n)
    uLaxFriedreich(M+1,n)= -c*(uLaxFriedreich(M+1,n)-uLaxFriedreich(M,n))*deltaT/deltaX + uLaxFriedreich(M+1,n)
    uLaxWendroff(M+1,n)= -c*(uLaxWendroff(M+1,n)-uLaxWendroff(M,n))*deltaT/deltaX + uLaxWendroff(M+1,n)
            
    tn = tn + deltaT
    
    
end


//scf(1)
//xtitle('Schéma decentré','x','y')
//plot2d(x,uDecentre(:,26))
//plot2d(x,Uex(x,tn),style = 6)
//
//
//scf(2)
//xtitle('Schéma Lax-Friedreich','x','y')
//plot2d(x,uLaxFriedreich(:,26))
//plot2d(x,Uex(x,tn),style=6)
//
//scf(3)
//xtitle('Schéma Lax-Xendroff','x','y')
//plot2d(x,uLaxWendroff(:,26))
//plot2d(x,Uex(x,tn),style=6)
//
scf(4)
tp=N;
xtitle('Graph de U pour t=T et Δt=0.8*Δx/c')
plot2d(xx,uExact(:,tp),style = 6) // HABBAL
plot2d(xx,uDecentre(:,tp),style=3)
plot2d(xx,uLaxFriedreich(:,tp),style=2)
plot2d(xx,uLaxWendroff(:,tp))
legend(["U exacte";"U décentré";"U Lax-Friedreich";"U Lax-Wendroff"],[0.1,0.3])
