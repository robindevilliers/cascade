package uk.co.test.cascade;

public interface Context {

    void preSetup();

    void postSetup();


    void postExecute();


    void preCleanup();

    void postCleanup();
}
