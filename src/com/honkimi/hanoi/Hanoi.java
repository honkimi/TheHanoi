package com.honkimi.hanoi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Hanoi {
    public HashMap<String, Poll> polls;
    public int num;

    public  List<String[]> moveList;

    public Hanoi(int num) {
        this.num = num;
        moveList = new ArrayList<String[]>();
        polls = new HashMap<String, Poll>();
        polls.put("S", new Poll("S"));
        polls.put("G", new Poll("G"));
        polls.put("B", new Poll("B"));

        for (int i = 0; i < num; i++) {
            polls.get("S").push(num - i);
        }
    }

    private void move(Poll from, Poll to) {
        to.push(from.pop());
    }

    public void doHanoi(int n, Poll start, Poll goal, Poll buffer) {
        if (n > 0) {
            doHanoi(n-1, start, buffer, goal);
            move(start, goal);
            moveList.add(new String[] {start.name, goal.name});
            doHanoi(n-1, buffer, goal, start);
        }
    }

    class Poll {
        private Stack<Integer> stack;
        public String name;

        public Poll(String name) {
            stack = new Stack<Integer>();
            this.name = name;
        }

        public void push(int v) {
            if (!stack.isEmpty() && stack.peek() < v) {
                throw new RuntimeException();
            }
            stack.push(v);
        }

        public int pop() {
            return stack.pop();
        }
    }
}
