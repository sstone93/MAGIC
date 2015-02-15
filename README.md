# MAGIC
For group 8 of 3004 Winter 2015. Group members: Chantal Forget, Shannon Stone, Michael Catt, Nick Laws

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
