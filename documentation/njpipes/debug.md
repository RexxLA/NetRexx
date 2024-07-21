# Debugging Pipelines

To find out whhat is happening in a pipeline, you can specify debug options.

|Option   |Effect   |
|---|---|---|
| 1  | Show all pipes starting  |
| 2  | Show all pipes ending  |
| 4  | Show all stages starting  |
| 8  | Show all stages stopping  |
| 16  | Show all Commit requests  |
| 32 | Show all Commit completions  |
| 64 | Show StageErrors raised via stage's Error(int,String) method.[^error]|
| 128 | Show the argument that each stage is receiving.[^handy]|


[^error]: The stage class uses Error for all its StageError signals
[^handy]:   Handy since
            shells have a habit of doing unexpected thing to arguments.
            (try: java findtext exit *.nrx vs java findtext "exit *.nrx")
			
To create a flag to see all stages starting and stopping you would
    add 8+4 and use:

    - pipe (apipe debug 12) ...

## The dump() stage
The second option is to use the invoke the dump() method in a stage. This dumps the status of the pipe using the same format you see when a pipe deadlocks.  Using dump() does not normally cause a pipe to terminate.  Once in a while dump() will generate an exception.  This happens since dump() does not use protect or synchronize so it does not stall.
