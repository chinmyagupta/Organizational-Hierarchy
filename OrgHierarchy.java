import java.io.*;
import java.util.*;



// Tree node
class Node {
    int id;
    int level;
    Node boss;
    LinkedList children;
}


public class OrgHierarchy implements OrgHierarchyInterface {

    Node root;  //root node
    int tree_size = 0;  // We will store number of people in this private variable.
    AVL avl = new AVL();  // The AVL tree of all the nodes. Nodes will be compared using their ids.

    public boolean isEmpty() { // O(1)
        return root == null; // If the organisation is empty, that means there's no owner and vice-versa.
    }

    public int size() { // O(1)
        return tree_size; // As we are storing number of people in tree_size.
    }

    public int level(int id) throws IllegalIDException { // O(logN)
        Node node = avl.getNodeFromId(id);  // Finding node with the given id.
        if (node == null) throw new IllegalIDException("Given ID doesn't exist."); // If node doesn't exist.
        return node.level;
    }

    public void hireOwner(int id) throws NotEmptyException { // O(logN)
        // Throwing an error if the organisation already has an owner.
        if (!isEmpty()) throw new NotEmptyException("Owner already exists.");

        root = new Node();
        root.id = id;
        root.level = 1;
        root.children = new LinkedList();
        // Created root node with the given id and set up its level as 1 and children as an empty list.

        // Now adding the first node to the AVL tree.
        avl.root = avl.insert(avl.root, root);
        tree_size++; // Finally, updated tree_size.
    }

    public void hireEmployee(int id, int bossid) throws IllegalIDException { // O(logN)
        Node boss = avl.getNodeFromId(bossid); // Getting boss's node using its id.
        if (boss == null) throw new IllegalIDException("Boss ID doesn't exist.");
        Node d = avl.getNodeFromId(id);  // Making sure that given id doesn't already exist.
        if (d != null) throw new IllegalIDException("ID already exists.");

        Node node = new Node();
        node.id = id;
        node.level = boss.level + 1;
        node.boss = boss;
        node.children = new LinkedList();
        // Created new node with the given id and set up its level, boss as given and children as an empty list.

        // Now adding node to the AVL tree and in list of children of its boss.
        boss.children.add(node);
        avl.root = avl.insert(avl.root, node);
        tree_size++; // Finally, updated tree_size.

    }

    public void fireEmployee(int id) throws IllegalIDException { // O(logN + len(children))
        Node node = avl.getNodeFromId(id); // Finding node with the given ID.
        if (node == null) throw new IllegalIDException("ID doesn't exist.");
        if (node.id == root.id) throw new IllegalIDException("Owner can't be fired.");

        // Deleting the node from the AVL tree and list of children of its boss.
        avl.root = avl.delete(avl.root, id);
        node.boss.children.remove(node);
        tree_size--; // Finally, updated tree_size.
    }

    public void fireEmployee(int id, int manageid) throws IllegalIDException { // O(logN + len(children))
        Node node = avl.getNodeFromId(id); // Getting the node with the id to be deleted.
        if (node == null) throw new IllegalIDException("ID doesn't exist.");

        Node new_boss = avl.getNodeFromId(manageid); // Getting the node associated with the new boss.
        if (new_boss == null) throw new IllegalIDException("ID doesn't exist.");

        if (new_boss.level != node.level) throw new IllegalIDException("Level of the two nodes are different.");

        // Deleting the node from the AVL tree and list of children of its boss.
        avl.root = avl.delete(avl.root, id);
        node.boss.children.remove(node);

        // Now moving all the children of the deleted node under new boss and updating their boss.
        ListNode d = node.children.head;
        while (d != null) {
            new_boss.children.add(d.node);
            d.node.boss = new_boss;
            d = d.next;
        }

        tree_size--; // Finally, updated tree_size.
    }

    public int boss(int id) throws IllegalIDException { // O(logN)
        Node node = avl.getNodeFromId(id);  // Finding node with the give ID.
        if (node == null) throw new IllegalIDException("ID doesn't exist.");
        if (node.id == root.id) return -1; // Returning -1, if given id is of owner.

        return node.boss.id; // Returning id of the boss.
    }

    public int lowestCommonBoss(int id1, int id2) throws IllegalIDException { // O(logN + node1.level + node2.level)
        Node node1 = avl.getNodeFromId(id1); // Getting node with id1.
        Node node2 = avl.getNodeFromId(id2); // Getting node with id2.
        if (node1 == null || node2 == null) throw new IllegalIDException("ID doesn't exist.");

        if (node1.id == 1 || node2.id == 1) return 1; // Directly returning if one of them is owner.

        // Moving the lower node up until they're on the same level.
        while (node1.level > node2.level) node1 = node1.boss;
        while (node2.level > node1.level) node2 = node2.boss;

        // Now they both are at the same level.
        // We'll keep moving to their bosses until we find the lowest common boss.
        node1 = node1.boss;
        node2 = node2.boss;
        while (node1.id != node2.id) {
            node1 = node1.boss;
            node2 = node2.boss;
        }
        return node1.id;
    }

    public String toString(int id) throws IllegalIDException { // O(N)
        Node node = avl.getNodeFromId(id); // Getting node associated with the give id.
        if (node == null) throw new IllegalIDException("ID doesn't exist."); // If none of the nodes have the given id.

        // We'll be running a BFS to get the nodes level-vise.
        // We will be using a linkedlist-queue to store the nodes.
        StringBuilder ans = new StringBuilder(node.id + "");
        LinkedList queue = new LinkedList(node.children);
        while (!queue.isEmpty()) {
            int size = queue.size();
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                Node d = queue.remove();
                queue.addAll(d.children);
                arr[i] = d.id;
            }

            // In array, we stored all the nodes in the current level and then we will move to the next level.
            mergeSort(arr, 0, arr.length - 1); // Sorted the array.
            for (int j : arr) {
                ans.append(" ").append(j);
            }
        }
        return ans.toString(); // Returning the string.
    }


    // HELPER FUNCTIONS ADDED BY ME

    void mergeSort(int[] arr, int l, int r) { // SORTS THE ARRAY.
        if (r <= l) return;
        int mid = l + (r - l) / 2;
        mergeSort(arr, l, mid);
        mergeSort(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }

    void merge(int[] arr, int l, int mid, int r) { // HELPS IN SORTING THE ARRAY
        int N1 = mid - l + 1;
        int N2 = r - mid;

        int[] first = new int[N1];
        int[] second = new int[N2];

        for (int i = 0; i < N1; i++) first[i] = arr[l + i];
        for (int i = 0; i < N2; i++) second[i] = arr[mid + 1 + i];

        int i = 0, j = 0;
        int k = l;
        while (i < N1 && j < N2) {
            if (first[i] <= second[j]) arr[k++] = first[i++];
            else arr[k++] = second[j++];
        }

        while (i < N1) arr[k++] = first[i++];
        while (j < N2) arr[k++] = second[j++];
    }


}
