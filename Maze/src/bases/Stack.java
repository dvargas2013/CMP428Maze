package bases;

/**
 * Just a regular Stack. You can pop and peek and all that good stuff.

 * @author danv
 *
 * @param <J> Any object You want. Enjoy.
 */
class Stack<J> {
	// The thing after the dummy Node is the actual first item
	private final Node<J> dummy = new Node<J>(null);
	private int size = 0;

	public J peek() {
		return (isEmpty()) ? dummy.item : dummy.next().item;
	}
	
	public void clear() {
		size = 0;
		dummy.setNext(null);
	}

	public J pop() {
		if (isEmpty())
			return dummy.item;
		J m = dummy.next().item;
		dummy.setNext(dummy.next().next());
		size--;
		return m;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void push(J e) {
		dummy.setNext(new Node<J>(e, dummy.next()));
		size++;
	}

	public int getSize() {
		return size;
	}
}

class Node<E> {
    private Node<E> next; final E item;
    Node(E e) {next=null;item=e;}
    Node(E e, Node<E> n) {item=e;next=n;}
    Node<E> next() {return next;}
    void setNext(Node<E> n) {next=n;}
}