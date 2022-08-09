class ListNode {
    Node node;
    ListNode prev, next;

    ListNode(Node d) {
        node = d;
    }
}


public class LinkedList {
    ListNode head, tail;
    private int length;

    LinkedList(){
        length = 0;
    } // Initialises the list with length 0 if nothing is given.

    LinkedList(LinkedList list){ // It initialises the list using a given list. Copies the list into the new one.
        length = 0;
        ListNode node = list.head;
        while(node != null){
            add(node.node);
            node = node.next;
        }
    }

    boolean isEmpty() {
        return length == 0;
    }

    int size() { // Returns number of elements in the list.
        return length;
    }

    void add(Node node) {
        ListNode newNode = new ListNode(node);
        if (head == null) { // size is 0.
            head = newNode;
            tail = newNode;
        } else if (head == tail) { // size is 1.
            newNode.prev = head;
            head.next = newNode;
            tail = newNode;
            tail.next = null;
            head.prev = null;
        } else { // Adds the new node to the end and updates the tail.
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        length++;
    }

    void addAll(LinkedList list) { // Takes another list as input and adds all its elements to this one.
        ListNode node = list.head;
        while (node != null) {
            add(node.node);
            node = node.next;
        }
    }

    Node remove() { // Removes the last added element from the list. (AS IN QUEUE).
        Node node = head.node;
        if (length == 1) {
            head = null;
            tail = null;
        } else {
            head.next.prev = null;
            head = head.next;
        }
        length--;
        return node;
    }

    void remove(Node d){ // Removes the given node from the list.
        if (length == 1){
            head = null;
            tail = null;
            length = 0;
        }

        else if (length == 2){
            Node node;
            if (head.node == d) node = tail.node;
            else node = head.node;
            head = null;
            tail = null;
            add(node);
            length = 1;
        }

        else{
            ListNode node = head;

            while(node.node.id != d.id){
                node = node.next;
            }
            if (node == head){
                head = head.next;
                head.prev = null;
            }
            else if (node == tail){
                tail = tail.prev;
                tail.next = null;
            }
            else{
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            length--;
        }
    }

    void print(){ // Prints the list from head to tail.  Only used for debugging.

        if (head == null) System.out.println("NULL");

        ListNode node = head;
        while(node != null){
            System.out.print(node.node.id + " ");
            node = node.next;
        }
        System.out.println();
    }


}
