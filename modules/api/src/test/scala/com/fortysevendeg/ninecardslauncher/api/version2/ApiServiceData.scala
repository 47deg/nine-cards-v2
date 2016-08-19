package com.fortysevendeg.ninecardslauncher.api.version2

trait ApiServiceData {

  val baseUrl = "http://localhost:8080"

  val headerAuthToken = "X-Auth-Token"
  val headerSessionToken = "X-Session-Token"
  val headerAndroidId = "X-Android-ID"
  val headerMarketLocalization = "X-Android-Market-Localization"
  val headerMarketLocalizationValue = "en-US"
  val headerAndroidMarketToken = "X-Google-Play-Token"

  val statusCodeOk = 200

  val email = "email@dot.com"
  val loginId = "login-id"
  val tokenId = "token-id"
  val apiKey = "api-key"
  val deviceToken = "device-token"
  val sessionToken = "session-token"
  val androidId = "android-id"
  val marketToken = "market-token"

  val installationAuthToken = "f9dfeb34c16dae2715dc85b82f7d2a91ff3999212ed5235950d3328c0ab8e35886c98a788ed5fa35211be0a2b7829306b83c7cc4c954025d2b69786608666785"
  val latestCollectionsAuthToken = "eeec8eee60eba7e312cfc752396c0f05625bc372c95e1c6e70ea0e02b61f23e1f238b42c133fedcba2eb725c3e6a14542c2070d8db40e7ffa5a74eb8f50b8f60"
  val topCollectionsAuthToken = "7d7290b7c03da9cf5000b05c148ed430bb333c67a47d57085438481e05f683428b9ad662309fdb637f925bd1eb85b8eefcd522b206314977028f29724ca922d2"
  val collectionsAuthToken = "c5284c3a5ff576a984689bd13ff3e6144dc5ebfedcd14f4eb7ba5b8b8f5ff1211b677fdf12ff8fe93d226de1fb45c706578c4ee43d47dda5cde51c91a7c5bb06"
  val collectionsIdAuthToken = "5fdf0285acf5b1f0223c903553558a7512c92aabac6e6c2dd1daa794682966f954438645ce6cd139036ace029e38c609a23aaeabcdd453d25a5349d8b5cf178c"
  val categorizeAuthToken = "4f129e296588493aab55e0192894ed95867546674844479f8dce0f0f506eed80991a1f09d459d476335acdf27e3f178d16f8df94c45a0e77d4f10935f8199493"
  val recommendationsFreeAuthToken = "915f6db594dd9be2bc9cfb2a232c52bccee633afe97086cca7f468690ad8ca7bb87747b5ee13a35af0103091bf9b0d5a38f0cccd5179ca98b399c65f7afae6a2"
  val recommendationsPaidAuthToken = "bc84d348c49d1a5f6fb9694050ed503ff8d55f0b6163b7a6e3929287e33a72ccf1549d99345855e13a412724f56042970e66148f2ad712105001773884e8ceb4"
  val recommendationsAuthToken = "eb8d8a575642e6d1b4b048d921a5b5ec9036a10542e424077ba261fd8a55aa47287f202691ef7c8a4ec19013affbc0c01cfa13e9a759734f2579a869a1842361"

  val simpleHeader = SimpleHeader(apiKey, sessionToken, androidId)
  val headerWithMarketToken = HeaderWithMarketToken(apiKey, sessionToken, androidId, marketToken)

  val category = "SOCIAL"
  val publicIdentifier = "collection-public-identifier"
  val offset = 0
  val limit = 100

  val collectionName = "collection name"
  val collectionAuthor = "collection author"
  val collectionDescription = "collection description"
  val collectionIcon = "collection icon"

  val collectionApp = CollectionApp(
    stars = 3.5,
    icon = "app icon",
    packageName = "com.package.app",
    downloads = "500,000,000+",
    category = category,
    title = "app title",
    free = true)

  val collection = Collection(
    name = collectionName,
    author = collectionAuthor,
    description = Some(collectionDescription),
    icon = collectionIcon,
    category = category,
    community = true,
    publishedOn = "2016-08-16T14:55:30.574000",
    installations = Some(10),
    views = Some(100),
    publicIdentifier = publicIdentifier,
    appsInfo = Seq(collectionApp),
    packages = Seq(collectionApp.packageName))

  val createCollectionRequest = CreateCollectionRequest(
    name = collectionName,
    author = collectionAuthor,
    description = collectionDescription,
    icon = collectionIcon,
    category = category,
    community = true,
    packages = Seq(collectionApp.packageName))

  val createCollectionResponse = CreateCollectionResponse(
    publicIdentifier = publicIdentifier,
    packagesStats = PackagesStats(1))

  val updateCollectionRequest = UpdateCollectionRequest(
    collectionInfo = Some(CollectionUpdateInfo(title = collectionName, description = Some(collectionDescription))),
    packages = Some(Seq(collectionApp.packageName)))

  val updateCollectionResponse = UpdateCollectionResponse(
    publicIdentifier = publicIdentifier,
    packagesStats = PackagesStats(1))

  val categorizeRequest = CategorizeRequest(
    items = Seq(collectionApp.packageName))

  val categorizeResponse = CategorizeResponse(
    errors = Seq.empty,
    items = Seq(CategorizedApp(packageName = collectionApp.packageName, category = category)))

  val recommendationsResponse = RecommendationsResponse(
    apps = Seq(RecommendationApp(
      packageName = collectionApp.packageName,
      name = collectionApp.title,
      downloads = collectionApp.downloads,
      icon = collectionApp.icon,
      stars = collectionApp.stars,
      free = collectionApp.free,
      description = "Application description",
      screenshots = Seq("screenshot1", "screenshot2", "screenshot3")
    ))
  )

}
