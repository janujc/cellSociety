Longest Method Review
===

Januario Carreiro (jjc70)
Anshu Dwibhashi (ad353)
Jonathan Yu (jy178)

Issues:

Method parseConfigFile() on line 33 of `ConfigParser` was over 130 lines long. Had several nested try-catch and if trees, and several of the if
trees included duplicated code. Furthermore, the single public parser method did everything, as opposed to
delegating work to several different private parser classes. Lastly, there was a try-catch that was 
never used---all the other ~130 lines of the method were in the body of the try-catch. 

Changes:

**Added two more private methods** that will be called by parseConfigFile(): packageReturnable()
and parseGridInfo(). packageReturnable() especially was made especially to **reduce the code duplications** found in 
parseConfigFile(). parseGridInfo() made parseConfigFile() be easier to understand because now parseGridInfo() is 
the main method used to parse information about the grid itself. If we had more times, we would have created 
more submethods, one for each different "piece" of information that parseConfigFile() parses.
All our changes **reduced the size of parseConfigFile by over 45%**. 

Resolutions:

Reduce duplication through the use of template methods

If a method does too many things, refactor it into several smaller methods doing a more specific thing

Don't nest try-catch unless absolutely necessary.