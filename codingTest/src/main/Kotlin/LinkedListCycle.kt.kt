

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

class LinkedListCycle {
    fun hasCycle(head: ListNode?): Boolean {
        var node: ListNode? = head
        var set: HashSet<ListNode> = hashSetOf()
        while (node != null){
            if(set.contains(node))
                return true
            set.add(node)
            node = node?.next
        }
        return false
    }
}