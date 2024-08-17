# Error Messages

The pipes compiler can issue the following error messages:

| Msg |Meaning | 
|---|---|
|pipc001e| Name of pipe is missing|
|pipc002e| Runaway time must be numeric|
|pipc003e| Stall time must be numeric|
|pipc004e| Debug level must be numeric, found 'lvl'|
|pipc005e| Pipe statement within a pipe specification|
|pipc006e| Cache must be followed by a valid symbol, found 'work'|
|pipc007e| Append or prefix stages cannot be labeled 'stg.word(1)'|
|pipc008e| Missing stage/pipe after 'stg.word(1)'|
|pipc009e| Missing range and/or stage/pipe after 'stg.word(1)'|
|pipc010e| Specs has only one output stream, NOT requires two|
|pipc011e| Pipe definition 'stg' must be terminated by 'pend'|
|pipc012e| Pipe definition 'stg' must define a pipe|
|pipc013e| Label 'label' already used|
|pipc014e| Label 'label' must not be numeric|
|pipc015e| Pipe 'stg' cannot be labeled|
|pipc016e| Connectors must be named|
|pipc017e| Label 'label' not defined|
|pipc018e| Use a nop stage between 'label' definition a second use|
|pipc019e| Expected a colon after 'stg.word(1)'|
|pipc020e| Pipe as stage definition 'stg' is missing a period|
|pipc021e| 'parms' incorrect.  Use: {({class} {size})} {A|D} {target}|
|pipc022e| Need to use a nop stage between 's[c[i,j-1]]' 'ssep' 's[c[i,j]]'|
|pipc023e| Problem reading group rc='r'|
|pipc024e| 'w' is unrecognized |
|pipc025w| Warning: could not delete 'fileid'|
|pipc026e| 'key' is only valid in a class|
|pipc027w| Warning - Possible netrexx exit in 'arg()' at 'l'|
|pipc028e| Pipe name and parms must be on same line as 'key'|
|pipc029e| Pipe name missing for 'key'|
|pipc030e| Body of 'wp' is empty|
|pipc031e| *: connectors not implemented.  Use *in: or *out:|
|pipc032e| Connector 'w1' should start with *in or *out|
|pipc033e| Missing colon at 'w1'|
|pipc034e|  Connect 'w1' cannot contain a period|
|pipc035e| cannot connect 'in' to an input stream with 'key'|
|pipc036e| Pipe fragment 'sub' needs atleast one 'sep'|
|pipc037e| Cannot connect 'out' to an output stream with 'key'|
|pipc038e| A object name cannot contain spaces, found: 'a'|
|pipc039e| Duplicate pipe as stage at 'stg'|
|pipc040e| Missing ':' in connector at 'stg'|
|pipc041e| Cannot define connectors as stage labels in 'stg'|
|pipc042w| StageExits overloaded at 'stg'|
|pipc043e| Stage constructor must be (), found: 'stg'|
|pipc044i| Building pipe 'name'|
|pipc045i| Processing 'w' .njp|
|pipc046e| 'file' is unrecognized. Does the file exist?|
|pipc047e| An .nrx file exists. Please move it out of the way.|
|pipc048e| Run method not overridden by stage or  pipe|
|pipc049e| No outputs for eofReport(current) to report on|
|pipc050e| No outputs for eofReport(all)to report on|
|pipc051e| Invalid parm for eofReport|

Table: Pipes compiler error messages.
