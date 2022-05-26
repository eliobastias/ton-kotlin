package org.ton.cell

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CellTest {
    @Test
    fun `computation of maxDepth`() {
        val cell0 = Cell("_")
        assertEquals(0, cell0.maxDepth)

        val cell1 = Cell("_", cell0)
        assertEquals(1, cell1.maxDepth)

        val cell2 = Cell("_", cell0, cell1)
        assertEquals(2, cell2.maxDepth)

        val cell3 = Cell("_", cell0, cell1, cell2)
        assertEquals(3, cell3.maxDepth)

        val cell4 = Cell("_", cell0, cell1, cell2, cell3)
        assertEquals(4, cell4.maxDepth)

        val cell5 = Cell(
            "_",
            Cell(
                "_",
                Cell("_"),
                Cell(
                    "_",
                    Cell(
                        "_",
                        Cell("_")
                    )
                )
            ),
            Cell("_"),
            Cell(
                "_",
                Cell("_"),
                Cell(
                    "_",
                    Cell(
                        "_",
                        Cell("_", Cell("_"))
                    )
                )
            )
        )
        assertEquals(5, cell5.maxDepth)
    }

    @Test
    fun `cell traversing with treeWalk()`() {
        val cell = Cell(
            "0",
            Cell(
                "1",
                Cell("2"),
                Cell("3")
            ),
            Cell("4"),
            Cell(
                "5",
                Cell("6"),
                Cell(
                    "7",
                    Cell("8")
                )
            )
        )

        assertContentEquals(
            sequenceOf("1", "4", "5", "2", "3", "6", "7", "8"),
            cell.treeWalk().map { it.bits.toString() }.asSequence()
        )
    }

    @Test
    fun `computation of cell descriptors d1, d2`() {
        val cell0 = Cell("_")
        assertContentEquals(
            sequenceOf(
                (0 + 8 * 0 + 16 * 0 + 32 * 0).toByte(), // references descriptor
                (0).toByte(), // bits descriptor
            ), cell0.descriptors().asSequence()
        )

        val cell1 = Cell("1")
        assertContentEquals(
            sequenceOf(
                (0 + 8 * 0 + 16 * 0 + 32 * 0).toByte(), // references descriptor
                (1 + 0).toByte(), // bits descriptor
            ), cell1.descriptors().asSequence()
        )


        val cell2 = Cell("23", cell1)
        assertContentEquals(
            sequenceOf(
                (1 + 8 * 0 + 16 * 0 + 32 * 0).toByte(), // references descriptor
                (1 + 1).toByte(), // bits descriptor
            ), cell2.descriptors().asSequence()
        )

        val cell3 = Cell("4567", cell0, cell1, cell2)
        assertContentEquals(
            sequenceOf(
                (3 + 8 * 0 + 16 * 0 + 32 * 0).toByte(), // references descriptor
                (2 + 2).toByte(), // bits descriptor
            ), cell3.descriptors().asSequence()
        )
    }
}
