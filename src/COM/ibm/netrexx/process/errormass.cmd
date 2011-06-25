/**/
in=temp.dat
out=temp.out
do while lines(in)>0
  line=linein(in)
  parse var line pre '#k[  .]' post
  if post<>'' then do
    line="n=n+1; k[n]="post"; v[n]='   '"
    end
   else do
    parse var line 'D:\nrc\' dir
    if dir<>'' then line='--' dir
      else line='???' line
    end
  call lineout out, line
  end

call lineout out

