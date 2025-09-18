import kotlin.math.max

data class Node(
    val value: Int,
    var left: Node? = null,
    var right: Node? = null,

    ) {
    val height: Int get() = max(left?.height ?: 0, right?.height ?: 0) + 1
    val balance: Int get() = (right?.height ?: 0) - (left?.height ?: 0)


    fun toString(prefix: String): String =
        buildString {
            appendLine("$prefix + $value")
            left?.let { this.append(it.toString(prefix + "\t")) }
            right?.let { this.append(it.toString(prefix + "\t")) }
        }


    override fun toString(): String = toString("")


}

class AvlTree {
    var root: Node? = null

    val height
        get() = root?.height ?: 0


    fun insert(value: Int) {
        root = insert(root, value)
    }

    private fun insert(root: Node? = null, value: Int): Node {
        if (root == null) {
            return Node(value)
        }

        // Если число больше, то вправо
        if (root.value < value) {
            root.right = insert(root.right, value)
        } else if (root.value > value) {
            root.left = insert(root.left, value)
        } else {
            return root
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
        }
    }
}

private fun AvlTree.interactiveAdd() {
    var num: Int? = null
    while (num == null) {
        println("Введите целое число")
        num = readln().toIntOrNull() ?: continue
    }
    this.insert(num)
}
