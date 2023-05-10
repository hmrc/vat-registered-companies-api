resolvers += "HMRC Releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"
resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)
resolvers += Resolver.jcenterRepo

addSbtPlugin("com.github.sbt"    %  "sbt-release"           % "1.1.0")
addSbtPlugin("com.typesafe.play" %  "sbt-plugin"            % "2.8.16")
addSbtPlugin("uk.gov.hmrc"       %  "sbt-distributables"    % "2.2.0")
addSbtPlugin("net.virtual-void"  %  "sbt-dependency-graph"  % "0.10.0-RC1")
addSbtPlugin("org.scoverage"     %  "sbt-scoverage"         % "1.8.2")
addSbtPlugin("org.scalastyle"    %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("uk.gov.hmrc"       %  "sbt-auto-build"        % "3.9.0")
