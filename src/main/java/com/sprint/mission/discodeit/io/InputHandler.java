package com.sprint.mission.discodeit.io;

import java.util.Scanner;

public class InputHandler {
    private Scanner sc;
    public InputHandler(){
        sc = new Scanner(System.in);
    }
    public String getNewInput(){
        System.out.println("new Nickname :");
        return sc.next();
    }
    public String getYesNOInput(){
        System.out.println("Are you sure you want to delete it?\n");
        return sc.next();
    }

}
