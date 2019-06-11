package edu.skku.MAP.teamprojectmanager;

public class Act {
    private String NAME;
    private String Description;
    private String Worker;

    public Act () { }

    public Act(String NAME, String Description, String Worker) {
        this.NAME = NAME;
        this.Description = Description;
        this.Worker = Worker;
    }

    public String getNAME(){return NAME;}
    public String getDescription(){return Description;}
    public String getWorker(){return Worker;}
}


