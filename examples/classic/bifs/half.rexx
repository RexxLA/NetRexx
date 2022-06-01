/* rexx */
parse arg number
if datatype(number,'whole')
then say number%2 + number //2
else say ' argument is not a whole number'

