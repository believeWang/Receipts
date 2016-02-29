package com.practice.robert.receipts;

/**
 * Created by user on 2016/2/29.
 */
public class Prize {
    private String name;
    private String numbers;
    public Prize() {

    }

    public Prize(String name) {
        this.name = name;

    }
    public Prize(String name,String numbers) {
        this.name = name;
        this.numbers = numbers;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }
    public String toString(){
        return "["+this.name+":"+this.numbers+"]";


    }
}
