package com.sprint.mission.discodeit.io;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class InputHandler {
    private Scanner sc;
    public InputHandler(){
        sc = new Scanner(System.in);
    }
    public String getNewInput(){
        System.out.println("update :");
        return sc.next();
    }
    public String getYesNOInput(){
        System.out.println("Y/N \n");
        return sc.next();
    }
}
