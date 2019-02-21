API Review
===

Januario Carreiro (jjc70)
Diego Chamorro (dfc15)

# Part 1
* What about your API/design is intended to be flexible?

    The external API should not be flexible---the front    and back end should have agreed-upon standards for communication. The internal API, on the other hand, should be flexible because it should be organic to add features to the program.

* How is your API/design encapsulating your implementation decisions?

    Many decision are determined by the front end and back end external API and the front end internal API. Because we want our API to be flexible, and for the addition of extra features to be easy, the visualization will have a lot of abstraction. To add new parts to the visualization, one needs only to create a subclass with the necessary components. As for the external API, that impacts our implementation decision because it determines what methods need to be public/private and where abstraction could be useful. Further, objects passed between the front end and back end should be as general as possible in case we decide to change which data structures we are using later on.

* What exceptions (error cases) might occur in your part and how will you handle them (or not, by throwing)?

    The inputted command might not exist, and the error is caught by the back end which tells the front end to print an error message to console.

* Why do you think your API/design is good (also define what your measure of good is)?

    Our measure of good is to be flexible and explicit. Explicit in the sense that the communication is clear, and flexible in the sense that adding more features is easy.
    The external API is rigid in its communication which we believe is good design.
    Moreover, the internal API is flexible because adding new features is easy.