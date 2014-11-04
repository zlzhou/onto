%% sample file for Appendix B

father(louis, ken).
father(louis, paul).
father(ken, ron).
father(ken, mark).

grandfather(X,Z) :- father(X,Y), father(Y,Z).

