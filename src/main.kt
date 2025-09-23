import kotlin.math.max

data class Node(
    var value: Int,
    var left: Node? = null,
    var right: Node? = null,

    ) {
    val height: Int get() = max(left?.height ?: 0, right?.height ?: 0) + 1
    val balance: Int get() = (right?.height ?: 0) - (left?.height ?: 0)

    val hasChild: Boolean get() = right != null || left != null

    fun toString(prefix: String, perValuePrefix: String): String =
        buildString {
            appendLine("$prefix $perValuePrefix $value")
            left?.let { this.append(it.toString("$prefix\t", "l")) }
            right?.let { this.append(it.toString("$prefix\t", "r")) }
        }


    override fun toString(): String = toString("", perValuePrefix = "")


}

class AvlTree {
    var root: Node? = null

    val height
        get() = root?.height ?: 0


    fun insert(value: Int) {
        root = insert(root, value)
    }

    private fun insert(root: Node? = null, value: Int): Node {
        // Если корня нет
        if (root == null) {
            return Node(value)
        }

        // Если число больше, то вправо
        if (root.value < value) {
            root.right = insert(root.right, value)
        } else if (root.value > value) { //  меньше влево
            root.left = insert(root.left, value)
        } else {
            return root // Только уникальные значения
        }
        return balance(root)
    }

    // Поднимаем левый элемент
    private fun rotateRight(node: Node): Node? {
        val newNode = node.left
        node.left = newNode?.right
        newNode?.right = node
        return newNode
    }

    private fun rotateLeft(node: Node): Node? {
        val newNode = node.right
        node.right = newNode?.left
        newNode?.left = node
        return newNode
    }

    // Балансировка дерева
    private fun balance(node: Node): Node {
        if (node.balance == 2) { // Тогда правое больше
            if ((node.right?.balance ?: 0) < 0) { // Необходим большой поворот
                node.right = rotateRight(node.right!!)
            }
            return rotateLeft(node)!!
        } else if (node.balance == -2) {
            if ((node.left?.balance ?: 0) > 0) {
                node.left = rotateLeft(node.left!!)
            }
            return rotateRight(node)!!
        }
        return node // Нет необходимости балансировки
    }

    fun findParentNodeByValue(value: Int): Node? {
        if (root == null || value == root?.value) return null
        var current = root!!
        while ((current.left?.value != value && current.right?.value != value) && !current.hasChild) {
            if (value > current.value) {
                current = current.right!!
            } else {
                current = current.left!!
            }
        }
        return current
    }

    fun findParentNode(node: Node): Node? {
        return findParentNodeByValue(node.value)
    }

    fun delete(value: Int) {
        root = delete(root, value)
    }

    /**
     * Рекурсивная функция удаления
     */
    private fun delete(node: Node?, value: Int): Node? {
        if (node == null) {
            return null
        }
        val updatedNode: Node?
        if (value > node.value) {
            node.right = delete(node.right, value)
            updatedNode = node
        }else if (value < node.value){
            node.left = delete(node.left, value)
            updatedNode = node
        }else{
            if (node.left == null || node.right == null){
                updatedNode = node.left ?: node.right
            }else{
                val minNode = minNode(node.right!!)
                node.value = minNode.value
                node.right = delete(node.right, minNode.value)
                updatedNode = node
            }
        }
        return balance(updatedNode ?: return null)
    }

    fun minNode(node: Node): Node {
        var current = node
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }


    override fun toString(): String {
        return root.toString()
    }

}


fun main() {
    val tree = AvlTree()

    while (true) {
        println(tree)
        println("Что вы хотите сделать?")
        println("1. Добавить элемент")
        println("2. Удалить элемент")
        println("3. Найти элемент")
        println("4. Вывести дерево")
        println("5. Обойти дерево прямым обходом")
        println("6. Выполнить задание")
        println("7. Выйти")
        val variant = readln().toIntOrNull() ?: continue
        when (variant) {
            1 -> tree.interactiveAdd()
            2 -> tree.interactiveDelete()
        }
    }
}

private fun AvlTree.interactiveDelete() {
    var num: Int? = null
    while (num == null) {
        println("Введите целое число, которое вы хотите удалить")
        num = readln().toIntOrNull() ?: continue
    }
    this.delete(num)
}

private fun AvlTree.interactiveAdd() {
    var num: Int? = null
    while (num == null) {
        println("Введите целое число")
        num = readln().toIntOrNull() ?: continue
    }
    this.insert(num)
}
