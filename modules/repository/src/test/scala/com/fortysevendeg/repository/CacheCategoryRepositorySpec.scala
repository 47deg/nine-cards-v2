package com.fortysevendeg.repository

import com.fortysevendeg.ninecardslauncher.repository.commons.{CacheCategoryUri, ContentResolverWrapperImpl}
import com.fortysevendeg.ninecardslauncher.repository.model.{CacheCategory, CacheCategoryData}
import com.fortysevendeg.ninecardslauncher.repository.provider.CacheCategoryEntity.cacheCategoryEntityFromCursor
import com.fortysevendeg.ninecardslauncher.repository.provider._
import com.fortysevendeg.ninecardslauncher.repository.repositories._
import org.mockito.Mockito._
import org.specs2.matcher.DisjunctionMatchers
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Random

trait CacheCategoryMockCursor extends MockCursor with CacheCategoryTestData {

  val cursorData = Seq(
    (NineCardsSqlHelper.id, 0, cacheCategorySeq map (_.id), IntDataType),
    (CacheCategoryEntity.packageName, 1, cacheCategorySeq map (_.data.packageName), StringDataType),
    (CacheCategoryEntity.category, 2, cacheCategorySeq map (_.data.category), StringDataType),
    (CacheCategoryEntity.starRating, 3, cacheCategorySeq map (_.data.starRating), DoubleDataType),
    (CacheCategoryEntity.numDownloads, 4, cacheCategorySeq map (_.data.numDownloads), StringDataType),
    (CacheCategoryEntity.ratingsCount, 5, cacheCategorySeq map (_.data.ratingsCount), IntDataType),
    (CacheCategoryEntity.commentCount, 6, cacheCategorySeq map (_.data.commentCount), IntDataType)
  )

  prepareCursor[CacheCategory](cacheCategorySeq.size, cursorData)
}

trait EmptyCacheCategoryMockCursor extends MockCursor with CacheCategoryTestData {

  val cursorData = Seq(
    (NineCardsSqlHelper.id, 0, Seq.empty, IntDataType),
    (CacheCategoryEntity.packageName, 1, Seq.empty, StringDataType),
    (CacheCategoryEntity.category, 2, Seq.empty, StringDataType),
    (CacheCategoryEntity.starRating, 3, Seq.empty, DoubleDataType),
    (CacheCategoryEntity.numDownloads, 4, Seq.empty, StringDataType),
    (CacheCategoryEntity.ratingsCount, 5, Seq.empty, IntDataType),
    (CacheCategoryEntity.commentCount, 6, Seq.empty, IntDataType)
  )

  prepareCursor[CacheCategory](0, cursorData)
}

trait CacheCategoryTestData {
  val cacheCategoryId = Random.nextInt(10)
  val nonExistingCacheCategoryId = 15
  val packageName = Random.nextString(5)
  val nonExistingPackageName = Random.nextString(5)
  val category = Random.nextString(5)
  val starRating = Random.nextDouble()
  val numDownloads = Random.nextString(5)
  val ratingsCount = Random.nextInt(1)
  val commentCount = Random.nextInt(1)

  val cacheCategoryEntitySeq = createCacheCategoryEntitySeq(5)
  val cacheCategoryEntity = cacheCategoryEntitySeq.head
  val cacheCategorySeq = createCacheCategorySeq(5)
  val cacheCategory = cacheCategorySeq.head

  def createCacheCategoryEntitySeq(num: Int) = (0 until num) map (i => CacheCategoryEntity(
    id = cacheCategoryId + i,
    data = CacheCategoryEntityData(
      packageName = packageName,
      category = category,
      starRating = starRating,
      numDownloads = numDownloads,
      ratingsCount = ratingsCount,
      commentCount = commentCount)))

  def createCacheCategorySeq(num: Int) = (0 until num) map (i => CacheCategory(
    id = cacheCategoryId + i,
    data = CacheCategoryData(
      packageName = packageName,
      category = category,
      starRating = starRating,
      numDownloads = numDownloads,
      ratingsCount = ratingsCount,
      commentCount = commentCount)))

  def createCacheCategoryValues = Map[String, Any](
    CacheCategoryEntity.packageName -> packageName,
    CacheCategoryEntity.category -> category,
    CacheCategoryEntity.starRating -> starRating,
    CacheCategoryEntity.numDownloads -> numDownloads,
    CacheCategoryEntity.ratingsCount -> ratingsCount,
    CacheCategoryEntity.commentCount -> commentCount)
}

trait CacheCategoryTestSupport
  extends BaseTestSupport
  with CacheCategoryTestData
  with DBUtils
  with Mockito {

  lazy val contentResolverWrapper = mock[ContentResolverWrapperImpl]
  lazy val cacheCategoryRepository = new CacheCategoryRepository(contentResolverWrapper)

  def createCacheCategoryData = CacheCategoryData(
    packageName = packageName,
    category = category,
    starRating = starRating,
    numDownloads = numDownloads,
    ratingsCount = ratingsCount,
    commentCount = commentCount)

  when(contentResolverWrapper.insert(CacheCategoryUri, createCacheCategoryValues)).thenReturn(cacheCategoryId)

  when(contentResolverWrapper.deleteById(CacheCategoryUri, cacheCategoryId)).thenReturn(1)

  when(contentResolverWrapper.delete(
    CacheCategoryUri,
    where = s"${CacheCategoryEntity.packageName} = ?",
    whereParams = Seq(packageName))).thenReturn(1)

  when(contentResolverWrapper.findById(
    nineCardsUri = CacheCategoryUri,
    id = cacheCategoryId,
    projection = CacheCategoryEntity.allFields)(
      f = getEntityFromCursor(cacheCategoryEntityFromCursor))).thenReturn(Some(cacheCategoryEntity))

  when(contentResolverWrapper.findById(
    nineCardsUri = CacheCategoryUri,
    id = nonExistingCacheCategoryId,
    projection = CacheCategoryEntity.allFields)(
      f = getEntityFromCursor(cacheCategoryEntityFromCursor))).thenReturn(None)

  when(contentResolverWrapper.fetchAll(
    nineCardsUri = CacheCategoryUri,
    projection = CacheCategoryEntity.allFields)(
      f = getListFromCursor(cacheCategoryEntityFromCursor))).thenReturn(cacheCategoryEntitySeq)

  when(contentResolverWrapper.fetch(
    nineCardsUri = CacheCategoryUri,
    projection = CacheCategoryEntity.allFields,
    where = s"${CacheCategoryEntity.packageName} = ?",
    whereParams = Seq(packageName))(
      f = getEntityFromCursor(cacheCategoryEntityFromCursor))).thenReturn(Some(cacheCategoryEntity))

  when(contentResolverWrapper.fetch(
    nineCardsUri = CacheCategoryUri,
    projection = CacheCategoryEntity.allFields,
    where = s"${CacheCategoryEntity.packageName} = ?",
    whereParams = Seq(nonExistingPackageName))(
      f = getEntityFromCursor(cacheCategoryEntityFromCursor))).thenReturn(None)

  when(contentResolverWrapper.updateById(CacheCategoryUri, cacheCategoryId, createCacheCategoryValues)).thenReturn(1)
}

class CacheCategoryRepositorySpec
  extends Specification
  with Mockito
  with DisjunctionMatchers
  with CacheCategoryTestSupport {

  "CacheCategoryRepositoryClient component" should {

    "addCacheCategory should return a valid CacheCategory object" in {

      val result = runTask(cacheCategoryRepository.addCacheCategory(data = createCacheCategoryData))

      result must be_\/-[CacheCategory].which {
        cacheCategory =>
          cacheCategory.id shouldEqual cacheCategoryId
          cacheCategory.data.packageName shouldEqual packageName
      }
    }

    "deleteCacheCategory should return a successful response when a valid cache category id is given" in {
      val result = runTask(cacheCategoryRepository.deleteCacheCategory(cacheCategory = cacheCategory))

      result must be_\/-[Int].which {
        deleted =>
          deleted shouldEqual 1
      }
    }

    "deleteCacheCategoryByPackage should return a successful response when a valid package name is given" in {
      val result = runTask(cacheCategoryRepository.deleteCacheCategoryByPackage(packageName = packageName))

      result must be_\/-[Int].which {
        deleted =>
          deleted shouldEqual 1
      }
    }

    "fetchCacheCategories should return all the cache categories stored in the database" in {
      val result = runTask(cacheCategoryRepository.fetchCacheCategories)

      result must be_\/-[Seq[CacheCategory]].which {
        cacheCategories =>
          cacheCategories shouldEqual cacheCategorySeq
      }
    }

    "findCacheCategoryById should return a CacheCategory object when a existing id is given" in {
      val result = runTask(cacheCategoryRepository.findCacheCategoryById(id = cacheCategoryId))

      result must be_\/-[Option[CacheCategory]].which {
        maybeCacheCategory =>
          maybeCacheCategory must beSome[CacheCategory].which { cacheCategory =>
            cacheCategory.id shouldEqual cacheCategoryId
            cacheCategory.data.packageName shouldEqual packageName
          }
      }
    }

    "findCacheCategoryById should return None when a non-existing id is given" in {
      val result = runTask(cacheCategoryRepository.findCacheCategoryById(id = nonExistingCacheCategoryId))

      result must be_\/-[Option[CacheCategory]].which {
        maybeCacheCategory =>
          maybeCacheCategory must beNone
      }
    }

    "fetchCacheCategoryByPackage should return a CacheCategory object when a existing package name is given" in {
      val result = runTask(cacheCategoryRepository.fetchCacheCategoryByPackage(packageName = packageName))

      result must be_\/-[Option[CacheCategory]].which {
        maybeCacheCategory =>
          maybeCacheCategory must beSome[CacheCategory].which { cacheCategory =>
            cacheCategory.id shouldEqual cacheCategoryId
            cacheCategory.data.packageName shouldEqual packageName
          }
      }
    }

    "fetchCacheCategoryByPackage should return None when a non-existing package name is given" in {
      val result = runTask(cacheCategoryRepository.fetchCacheCategoryByPackage(packageName = nonExistingPackageName))

      result must be_\/-[Option[CacheCategory]].which {
        maybeCacheCategory =>
          maybeCacheCategory must beNone
      }
    }

    "updateCacheCategory should return a successful response when the cache category is updated" in {
      val result = runTask(cacheCategoryRepository.updateCacheCategory(cacheCategory = cacheCategory))

      result must be_\/-[Int].which {
        updated =>
          updated shouldEqual 1
      }
    }

    "getEntityFromCursor should return None when an empty cursor is given" in
      new EmptyCacheCategoryMockCursor
        with Scope {
        val result = getEntityFromCursor(cacheCategoryEntityFromCursor)(mockCursor)

        result must beNone
      }

    "getEntityFromCursor should return a CacheCategory object when a cursor with data is given" in
      new CacheCategoryMockCursor
        with Scope {
        val result = getEntityFromCursor(cacheCategoryEntityFromCursor)(mockCursor)

        result must beSome[CacheCategoryEntity].which { cacheCategory =>
          cacheCategory.id shouldEqual cacheCategoryEntity.id
          cacheCategory.data shouldEqual cacheCategoryEntity.data
        }
      }

    "getListFromCursor should return an empty sequence when an empty cursor is given" in
      new EmptyCacheCategoryMockCursor
        with Scope {
        val result = getListFromCursor(cacheCategoryEntityFromCursor)(mockCursor)

        result shouldEqual Seq.empty
      }

    "getEntityFromCursor should return a CacheCategory sequence when a cursor with data is given" in
      new CacheCategoryMockCursor
        with Scope {
        val result = getListFromCursor(cacheCategoryEntityFromCursor)(mockCursor)

        result shouldEqual cacheCategoryEntitySeq
      }
  }

}
