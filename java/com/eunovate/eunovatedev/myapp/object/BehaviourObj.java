package com.eunovate.eunovatedev.myapp.object;

/**
 * Created by EunovateDev on 1/29/2016.
 */

public class BehaviourObj{

    public String behaviour=null;
    public int rating=0;
    public int behaviour_id;

    public void setBehaviourID(int id){this.behaviour_id=id;}
    public int getBehaviourID(){return behaviour_id;}
    public void setBehaviour(String behaviour){
        this.behaviour=behaviour;
    }
    public String getBehaviour(){
        return behaviour;
    }
    public void setRating(int rating){
        this.rating=rating;
    }
    public int getRating(){
        return rating;
    }

}
