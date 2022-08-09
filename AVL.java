// AVL is similar to as taught in the class.


class AVL_node {
    Node node;

    int value, height; // value variable will store the id of the node it is storing.
    AVL_node left, right;

    AVL_node(Node node1) {
        node = node1;
        height = 1;
        value = node.id;
    }
}


public class AVL {
    AVL_node root;

    private int height(AVL_node d) { // Returns the height of the AVL.
        if (d == null) return 0;
        return d.height;
    }

    private int balance(AVL_node d) { // Returns the balance of the node.
        // and abs(balance) should be less than 2.
        if (d == null) return 0;
        return height(d.left) - height(d.right);
    }

    private AVL_node rotateRight(AVL_node d) {
        AVL_node left = d.left;
        AVL_node left_right = left.right;

        left.right = d;
        d.left = left_right;

        d.height = Math.max(height(d.left), height(d.right)) + 1;
        left.height = Math.max(height(left.left), height(left.right)) + 1;

        return left;
    }

    private AVL_node rotateLeft(AVL_node d) {

        AVL_node right = d.right;
        AVL_node right_left = right.left;
        right.left = d;
        d.right = right_left;

        // Update heights
        d.height = Math.max(height(d.right), height(d.left)) + 1;
        right.height = Math.max(height(right.left), height(right.right)) + 1;

        // Return new root
        return right;
    }

    AVL_node insert(AVL_node sub_root, Node node) {

        if (sub_root == null) {
            return new AVL_node(node);
        }

        if (node.id < sub_root.value) sub_root.left = insert(sub_root.left, node);
        else sub_root.right = insert(sub_root.right, node);

        sub_root.height = Math.max(height(sub_root.left), height(sub_root.right)) + 1;

        int bal = balance(sub_root);


        if (bal > 1) {
            if (node.id < sub_root.left.value) return rotateRight(sub_root);
            else if (node.id > sub_root.left.value) {
                sub_root.left = rotateLeft(sub_root.left);
                return rotateRight(sub_root);
            }
        }
        if (bal < -1) {
            if (node.id > sub_root.right.value) return rotateLeft(sub_root);
            else if (node.id < sub_root.right.value) {
                sub_root.right = rotateRight(sub_root.right);
                return rotateLeft(sub_root);
            }
        }
        return sub_root;
    }

    private AVL_node leftMost(AVL_node node) {
        AVL_node cur = node;
        while (cur.left != null)
            cur = cur.left;
        return cur;
    }


    AVL_node delete(AVL_node node, int id) {
        if (node == null) return null;

        if (id < node.value) node.left = delete(node.left, id);
        else if (id > node.value) node.right = delete(node.right, id);

        else {
            if (node.left == null || node.right == null) {
                AVL_node tmp;
                if (node.left == null) tmp = node.right;
                else tmp = node.left;
                node = tmp;
            } else {
                AVL_node tmp = leftMost(node.right);
                node.value = tmp.value;
                node.node = tmp.node;
                node.right = delete(node.right, tmp.value);
            }
        }

        if (node == null) return null;

        node.height = Math.max(height(node.left), height(node.right)) + 1;

        int bal = balance(node);

        if (bal > 1 ){
            if (balance(node.left) >= 0) return rotateRight(node);
            else if (balance(node.left) < 0) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
        }

        if (bal < -1 ){
            if (balance(node.right) <= 0)return rotateLeft(node);
            if (balance(node.right) > 0) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
        }
        return node;
    }


    Node getNodeFromId(int id) {
        return getNode_helper(id, root);
    }

    private Node getNode_helper(int id, AVL_node sub_root) {
        if (sub_root == null) return null;
        if (sub_root.value == id) return sub_root.node;
        Node x = getNode_helper(id, sub_root.left);
        Node y = getNode_helper(id, sub_root.right);

        if (x == null) return y;
        return x;
    }


}
