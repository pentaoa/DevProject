package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;

import java.util.*;

public class GameAi {
    GameManager gameManager;
    Random random = new Random();

    public GameAi(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public int move() {
        Node root = new Node(gameManager.copy(), null, -1);
        for (int i = 0; i < 10; i++) {
            Node node = select(root);
            if (node.state.isGameOver()) continue;
            expand(node);
            Node randomNode = node.children.get(random.nextInt(node.children.size()));
            int result = simulate(randomNode);
            backpropagate(randomNode, result);
        }
        Node bestChild = root.children.stream().max(Comparator.comparing(c -> c.evaluation)).orElse(null);
        System.out.println("ai 搜索成功");
        if (bestChild != null) {
            return bestChild.moveDirection;
        }
        return -1; // No valid move found
    }

    private Node select(Node node) {
        while (!node.children.isEmpty()) {
            node = node.children.get(random.nextInt(node.children.size()));
        }
        return node;
    }

    private void expand(Node node) {
        for (int direction = 0; direction < 4; direction++) {
            GameManager newState = node.state.copy();
            switch (direction) {
                case 0 -> newState.fastMove(0);
                case 1 -> newState.fastMove(1);
                case 2 -> newState.fastMove(2);
                case 3 -> newState.fastMove(3);
            }
            if (!newState.equals(node.state)) { // Only add new state if it is different
                node.children.add(new Node(newState, node, direction + 1));
            }
        }
    }

    private int simulate(Node node) {
        GameManager copy = node.state.copy();
        while (!copy.isGameOver()) {
            int direction = random.nextInt(4);
            switch (direction) {
                case 0 -> copy.fastMove(0);
                case 1 -> copy.fastMove(1);
                case 2 -> copy.fastMove(2);
                case 3 -> copy.fastMove(3);
            }
        }
        return copy.getMaxNumber();
    }

    private void backpropagate(Node node, int result) {
        Node tempNode = node;
        while (tempNode != null) {
            tempNode.evaluation += result;
            tempNode.visits++;
            tempNode = tempNode.parent;
        }
    }

    private static class Node {
        GameManager state;
        Node parent;
        List<Node> children;
        int evaluation;
        int visits;
        int moveDirection; // Direction of move that led to this state

        Node(GameManager state, Node parent, int moveDirection) {
            this.state = state;
            this.parent = parent;
            this.children = new ArrayList<>();
            this.evaluation = 0;
            this.visits = 0;
            this.moveDirection = moveDirection;
        }
    }
}
