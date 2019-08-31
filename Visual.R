install.packages("strex")
install.packages("shiny")
install.packages("shinydashboard")
install.packages("tidyverse")
install.packages("igraph")
install.packages("dplyr")
install.packages("plyr")
library(strex)
library(shiny)
library(shinydashboard)
library(tidyverse)
library(plyr)
library(igraph)
library(dplyr)

airData <- read.csv("MainEdges.csv", header = TRUE, sep = ",")
names <- data.frame(unique(airData$ORIGIN_CITY_NAME))
names$ID <- seq.int(nrow(names)) - 1
names(names) <- c("City", "id")
sel <- paste(names$id, names$City, sep = " ", collapse = NULL)
paths <- read.csv("Output.csv", header = FALSE, sep = ",")
header <- dashboardHeader(title = "Visualization of Paths")
frow1 <- fluidRow(
  box(
    title = "Path to destination",
    status = "primary", 
    solidHeader = TRUE,
    collapsible = TRUE,
    plotOutput("path", height = "250px")
  ), 
  selectInput("destination", "Select Destination", sel),
  actionButton(
    inputId = "go",
    label = "Submit"
  )
)

ui <- dashboardPage(
  header, 
  dashboardSidebar(), 
  body <- dashboardBody(frow1)
)

server <- function(input, output) {
  edgeList <- function(df) {
    df <- df %>% select_if(~ !any(is.na(.)))
    sel <- as.character(df[, 1])
    for(i in 3:ncol(df)-1) {
      sel <- c(sel, as.character(df[,i]), as.character(df[,i]))
    }
    sel <- c(sel,as.character(df[, ncol(df)]))
    g <- graph(sel)
    return(g)
    
  }
  observeEvent(input$go, {
  number <- str_nth_number(input$destination, n = 1) + 1
  df <- paths[number, ]
  r <- edgeList(df)
  output$path <- renderPlot({
    plot(r, vertex.color = "purple", vertex.label.dist = 4, vertex.size = 30, edge.arrow.size = 0.7)
   })
  })
  
}

shinyApp(ui = ui, server = server)