/**/
in=list.dat
out=list.out
do while lines(in)>0
  line=linein(in)
  parse var line . . linenum . 'RxQuit(' post
  if post<>'' then do
    parse var post ',' ',' '''' key '''' rest
    if key='' then key='???'
    if pos(',',rest)=0 then rest=''
    outline=key rest '['linenum']'
    end
   else do
    parse var line 'D:\nrc\' dir
    if dir<>'' then outline='-----' dir '-----'
      else outline='!!!' line
    end
  call lineout out, outline
  end

call lineout out

