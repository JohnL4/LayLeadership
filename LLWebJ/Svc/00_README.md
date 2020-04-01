The services layer contains interfaces for the repository, among other things.

A "Data" layer will implement those interfaces, and can be injected into the servlets (which, I guess, are in a
"servlet" layer).