# MAGIC
For group 8 of 3004 Winter 2015. Group members: Chantal Forget, Shannon Stone, Michael Catt, Nick Laws

//////
TARGET GAMEFLOW: CORRECT ME IF I AM WRONG

1. ServerController is run (sets up server side)
2. ClientControllers are run (likely sticking with 2 for the demo)
3. Server will propmt clients to choose a class (low priority, we can just set 2 manually if the GUI cannot support this)
4. Server begins game loop:
	-asks clients for activities, waits until they are all recieved
	-handles activities
	-transmits results back to client (as far as they are concerned atleast)
	-asks clients for combat actions, waits until they are all recieved
	-handles combat
	-transmits results back to client, as far as they are concerned atleast
5. Game ends
6. Shutdown of all processes
///////

///////
Nick's Notes on Understanding the networking threading wierdness (mainly for personal use):
Note: Same for client and server
///////

1. Controller instanciates the NetworkEntity
2. NetworkEntity calls .start() on itself.
3. If there is not already a thread, one is created (Thread(this))	<- isntance of the NetworkEntity
4. The new thread is started. (the run method is invoked)
5. In new thread, run loops endlessly until the value of "thread" goes back to being null (which it wont.)
   On the Client, Run does nothing??????
6. stop sets the value of thread to null after properly closing down all networkthreads

Conclusion: To kill the thread, just set thread to null? What join() does
is it halts execution until the thread is done looping (thread == null), so 
some function must set that.

More: The threading is used to allow the entity to run in the background rather than taking focus.
