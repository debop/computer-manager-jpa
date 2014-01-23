package models

/**
 * domains.models.Page 
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2014. 1. 23. 오전 10:13
 */
case class Pager[T](items: Seq[T], pageNo: Int, offset: Long, total: Long) {

    lazy val prev: Option[Int] = Option(pageNo - 1).filter(_ >= 0)

    lazy val next: Option[Int] = Option(pageNo + 1).filter(_ => (offset + items.size) < total)
}