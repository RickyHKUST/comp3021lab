# comp3021lab

# Code Interview for Lab

##### Task 1 - Question

Write a readme about how you design and implement the lab.

Describe several difficult points and how you came up with a solution.

##### Task 1 - Answer

When I was implementing lab2, the largest difficulty was how to debug the generated equals() method.

Since the java generated equals() method checks (getClass()!=obj.getClass()) but not (!(obj instanceof Note)), 
false was always returned due to Object!=Note.

Therefore, I gotta change the method and let it checks (!(obj instanceof Note)) instead.

But after I figured out this problem, this lab is easy for me to do.