import kotlin.math.max
import kotlin.random.Random

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
        } else if (value < node.value) {
            node.left = delete(node.left, value)
            updatedNode = node
        } else {
            if (node.left == null || node.right == null) {
                updatedNode = node.left ?: node.right
            } else {
                val minNode = minNode(node.right!!)
                node.value = minNode.value
                node.right = delete(node.right, minNode.value)
                updatedNode = node
            }
        }
        return balance(updatedNode ?: return null)
    }

    data class FindResult(val hasItem: Boolean = false, val path: String = "", val steps: Int = 1) {
        fun copyAddPathAndStep(path: String): FindResult {
            return copy(path = path + this.path, steps = steps + 1)
        }
    }

    fun find(value: Int, node: Node? = root): FindResult {
        if (node == null) return FindResult()
        if (value > node.value) return find(value, node.right).copyAddPathAndStep("r")
        if (value < node.value) return find(value, node.left).copyAddPathAndStep("l")
        return FindResult(true)
    }

    fun minNode(node: Node): Node {
        var current = node
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }

    /**
     * Прямой обход дерева
     */
    fun traversalPreOrder(node: Node? = root): Array<Int> {
        if (node == null) return emptyArray()
        val valArr = arrayOf(node.value)
        return valArr + traversalPreOrder(node.left) + traversalPreOrder(node.right)
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
        println("4. Заполнить дерево случайными элементами")
        println("5. Обойти дерево прямым обходом")
        println("6. Выполнить задание")
        println("7. Выйти")
        val variant = readln().toIntOrNull() ?: continue
        when (variant) {
            1 -> tree.interactiveAdd()
            2 -> tree.interactiveDelete()
            3 -> tree.interactiveFind()
            4 -> tree.interactiveFillRandomValues()
            5 -> println(tree.traversalPreOrder().joinToString(", "))
            6 -> tree.doTask()
            7 -> break
        }
    }
}

private fun AvlTree.doTask() {
    TODO("Not yet implemented")
}

private fun AvlTree.interactiveFillRandomValues() {
    var num: Int? = null
    while (num == null) {
        println("Введите количество элементов, которые вы хотите сгенерировать")
        num = readln().toIntOrNull() ?: continue
        if (num < 0) {
            println("Число должно быть положительным")
            num = null
        }
    }
    repeat(num) {
        this.insert(Random.nextInt(-2 * num, 2 * num))
    }
}

private fun AvlTree.interactiveFind() {
    val num: Int = inputInt("Введите целое число, которое вы хотите найти")
    val findResult = find(num)
    println("Элемент " + if (findResult.hasItem) "" else "не" + " найден")
    println("Количество шагов: ${findResult.steps}")
    println("Путь до элемента: ${findResult.path}")

}

private fun AvlTree.interactiveDelete() {
    val num: Int = inputInt("Введите целое число, которое вы хотите удалить")

    this.delete(num)
}

private fun AvlTree.interactiveAdd() {
    val num: Int = inputInt("Введите целое число, которое вы хотите добавить")

    this.insert(num)
}

fun inputInt(q: String): Int {
    var num: Int? = null
    while (num == null) {
        println(q)
        num = readln().toIntOrNull() ?: continue
    }
    return num
}