install.packages("tidyverse")
install.packages("dplyr")
library(tidyverse)
library(plyr)

airData <- read.csv("MainEdges.csv", header = TRUE, sep = ",")
airData[is.na(airData)] <- 0
airData <- ddply(airData, c("ORIGIN_CITY_NAME", "DEST_CITY_NAME"), summarize, DEP_DELAY = sum(DEP_DELAY), DISTANCE = mean(DISTANCE))
names <- data.frame(unique(airData$ORIGIN_CITY_NAME))
names$ID <- seq.int(nrow(names)) - 1
names(names) <- c("City", "id")
airData$from <- names$id[match(airData$ORIGIN_CITY_NAME, names$City)]
airData$to <- names$id[match(airData$DEST_CITY_NAME, names$City)]
View(airData)
airData <- airData[c(5,6,1,2,3,4)]
View(airData)
names <- names[c(2,1)]
write.csv(names, file = "nodes.csv", row.names = FALSE)
write.csv(airData, file = "edges.csv", row.names = FALSE)