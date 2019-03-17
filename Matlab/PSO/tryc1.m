 i=0;
 k=0;
 for ci=2:0.1:5
    for i=1:100
        sample=(test(f,1000,ci,0.4,0.15,100,2));
    if sample(1) <= 514 && sample(1)>= 510 && sample(2)<= 406 && sample(2)>=402
        k=k+1;
    end
i=i+1;
    end
    display(ci);
c=k/100;
display(c);
k=0;
 end