
package funalgorithm

import java.util.Arrays

class Tree(val value: Int) {
  val children = ArrayList<Tree?>()
  fun toString(indent: Int): String {
    var childrenStr = ""
    for (child in children) {
      childrenStr += "\n${" ".repeat(indent)} |_ " + child?.toString(indent + 3)
    }
    return "${value}${childrenStr}"
  }
}

class UnionFind(size: Int) {
  val parent = Array(size, { i -> i })
  val sz = Array(size, { _ -> 1 })
  val trees = Array<Tree?>(size, {i -> Tree(i)})
  fun root(p: Int): Int {
    var j = p
    while (parent[j] != j) {
      trees[parent[j]]?.children?.remove(trees[j])
      parent[j] = parent[parent[j]]
      trees[parent[j]]?.children?.add(trees[j])
      j = parent[j]
    }
    return j
  }
  fun connected(p: Int, q: Int): Boolean {
    return root(p) == root(q)
  }
  fun union(p: Int, q: Int) {
    if (p == q) return
    val pr = root(p)
    val qr = root(q)
    if (pr == qr) return
    if (sz[pr] < sz[qr]) {
      trees[parent[pr]]?.children?.remove(trees[pr])
      trees[qr]?.children?.add(trees[pr])
      parent[pr] = qr
      sz[qr] += sz[pr]
    } else {
      trees[parent[qr]]?.children?.remove(trees[qr])
      trees[pr]?.children?.add(trees[qr])
      parent[qr] = pr
      sz[pr] += sz[qr]
    }
  }
  override fun toString(): String {
    var outStr = ""
    outStr += "Sizes  : ${Arrays.toString(sz)}\n"
    outStr += "Parents: ${Arrays.toString(parent)}\n"
    for (tree in toForest()) {
      outStr += "${tree?.toString(0)}\n"
    }
    return outStr
  }
  fun toForest(): List<Tree?> {
    val forest = mutableListOf<Tree?>()
    for (i in parent.indices) {
      if (i == parent[i]) {
        forest.add(trees[i])
      }
    }
    return forest
  }
}

class Executor {
  val ANSI_RESET = "\u001B[0m";
  val ANSI_BLACK = "\u001B[30m";
  val ANSI_RED = "\u001B[31m";
  val ANSI_GREEN = "\u001B[32m";
  val ANSI_YELLOW = "\u001B[33m";
  val ANSI_BLUE = "\u001B[34m";
  val ANSI_PURPLE = "\u001B[35m";
  val ANSI_CYAN = "\u001B[36m";
  val ANSI_WHITE = "\u001B[37m";
  val defaultSize = 10
  val commandRegex = "(\\s*u\\s+\\d+\\s+\\d+\\s*)|(\\s*r\\s+\\d+\\s*)|" + 
    "(\\s*c\\s+\\d+\\s*)|(\\s*q\\s*)"
  var unionFind = UnionFind(defaultSize)
  init {
    println("${ANSI_GREEN}Created a set with default size of ${defaultSize}.${ANSI_RESET}")
    print(unionFind)
    println("-----------------------------------------")
    printHelp()
  }
  fun inRange(p: Int, low: Int, high: Int): Boolean {
    if (p < low || p > high) {
      println("${ANSI_RED}Enter a valid number in range ${low..high}.${ANSI_RESET}")
      return false
    }
    return true
  }

  fun printHelp() {
    println("${ANSI_CYAN}Create a set with N nodes: c <N>")
    println("Union two nodes: u <index> <index>")
    println("Find the root of a node: r <index>")
    println("Quit program: q${ANSI_RESET}")
  }

  fun validateCommand(command: String?): Boolean {
    if (command == null || !commandRegex.toRegex().matches(command)) {
      println("${ANSI_RED}Enter a valid command:${ANSI_RESET}")
      printHelp()
      return false
    }
    return true
  }

  fun runCommand(commandAndArgs: List<String>): Int {
    when (commandAndArgs[0]) {
      "u" -> {
        val p = commandAndArgs[1].toInt()
        if (!inRange(p, 0, unionFind.sz.size - 1)) return 1
        val q = commandAndArgs[2].toInt()
        if (!inRange(q, 0, unionFind.sz.size - 1)) return 1
        unionFind.union(p, q)
        print(unionFind)
      }
      "r" -> {
        val r = commandAndArgs[1].toInt()
        if (!inRange(r, 0, unionFind.sz.size - 1)) return 1
        println(" = ${unionFind.root(r)}")
        print(unionFind)
      }
      "c" -> {
        val n = commandAndArgs[1].toInt()
        if (!inRange(n, 1, 1000)) return 1
        unionFind = UnionFind(n)
        print(unionFind)
      }
      "q" -> {
        return 2
      }
    }
    return 0
  }
}

fun main(args: Array<String>) {
  val executor = Executor()
  loop@ while (true) {
    print("> ")
    val inputCommand = readLine()?.trim()
    if (inputCommand == "") continue@loop
    if (!executor.validateCommand(inputCommand)) continue@loop
    val commandAndArgs = inputCommand!!.split("\\s+".toRegex())
    if(executor.runCommand(commandAndArgs) == 2) break
    println("-----------------------------------------")
  }
}
