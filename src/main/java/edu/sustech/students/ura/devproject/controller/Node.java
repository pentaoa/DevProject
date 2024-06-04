package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;

import java.util.ArrayList;
import java.util.List;

public class Node {
    GameManager state;
    Node parent;
    List<Node> children;
    int evaluation;

    public Node(GameManager state, Node parent) {
        this.state = state;
        this.parent = parent;
        this.children = new ArrayList<>();
        evaluation = 0;
    }
}
