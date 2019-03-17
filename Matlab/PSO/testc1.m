for ci=0:0.1:2
    display(ci);
    limit=(test(f,1000,ci,0.4,0.15,100,2));
    display(limit);
end
   