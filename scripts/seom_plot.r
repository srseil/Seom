seom_plot <- function(csvfile, parameter, output, title, nominal, extra) {
  library(ggplot2)
  library(scales)
  library(stringr)
  
  violinfill <- "gray"
  if (output == "morality") {
    violinfill <- "#60ff80"
  } else if (output == "stability") {
    violinfill <- "#6080ff"
  }
  
  #xlabels <- ""
  #if (extra == "sw1d") {
  #  xlabels <- c(sprintf("0.03\n(\u03C9 \u2248 -0.53)"),
  #               sprintf("0.07\n(\u03C9 \u2248 -0.27)"),
  #               sprintf("0.125\n(\u03C9 \u2248 0.02)"),
  #               sprintf("0.18\n(\u03C9 \u2248 0.23)"),
  #               sprintf("0.28\n(\u03C9 \u2248 0.51)"))
  #} else if (extra == "sw2d") {
  #  xlabels <- c(sprintf("0.008 (\u03C9 \u2248 -0.50)"),
  #               sprintf("0.04 (\u03C9 \u2248 -0.23)"),
  #               sprintf("0.09 (\u03C9 \u2248 -0.01)"),
  #               sprintf("0.17 (\u03C9 \u2248 0.27)"),
  #               sprintf("0.275 (\u03C9 \u2248 0.50)"))
  #}
  
  data <- read.csv(csvfile, check.names = FALSE)
  
  if (parameter == "learning rule") {
    data[, parameter] <- factor(data[, parameter],
                                levels = c("IBest", "IProb", "IAvg", "BestR"))
  } else if (parameter == "neighborhood" & extra == "2d") {
    data[, parameter] <- factor(data[, parameter],
                                levels = c("N4", "N12", "N24", "M8", "M24", "M48"))
  } else if (parameter == "wrap around" | parameter == "risk dominance") {
    data[, parameter] <- factor(data[, parameter],
                                levels = c("true", "false"))
  } else if (parameter == "degree interval") {
    data[, parameter] <- factor(data[, parameter],
                                levels = c("3-8", "4-7", "2-3", "5-6", "8-9"))
  } else {
    data[, parameter] <- as.factor(data[, parameter])
  }
  
  plot <- ggplot(data = data, aes(x = data[, parameter], y = data[, output]))
  plot <- plot + geom_vline(xintercept = nominal, linetype = "dotted")
  plot <- plot + geom_violin(alpha = 0.4, fill = violinfill)
  plot <- plot + geom_boxplot(width = 0.1)
  plot <- plot + stat_smooth(aes(x = as.numeric(data[, parameter]),
                                 y = data[, output]),
                             method = "loess",
                             geom = "line", se = FALSE,
                             alpha = 0.25, color = "black", size = 2)
  plot <- plot + stat_summary(fun = mean, color = "black", geom="point",
                              shape = 18, size = 3)
  #plot <- plot + stat_summary(fun = mean, geom = "line", aes(group=1))
  
  if (output == "morality") {
    plot <- plot + ylim(0.0, 1.0)
  } else if (output == "stability") {
    #plot <- plot + scale_y_continuous(trans = "log2",
    #                                  limits = c(2^-8, 1),
    #                                  breaks = c(2^0, 2^-2, 2^-4, 2^-6, 2^-8),
    #                                  #expand = c(0, 0),
    #                                  labels = trans_format("log2", math_format(2^.x)))
    plot <- plot + ylim(0.0, 1.0)
  }
  
  plot <- plot + ggtitle(title)
  plot <- plot + xlab(str_to_title(parameter))
  plot <- plot + ylab(output)
  plot <- plot + theme(panel.grid.major.x = element_blank(),
                       axis.title.y = element_text(size = 20, family = "Calibri"),
                       axis.title.x = element_text(size = 20, family = "Calibri"),
                       axis.text = element_text(size = 16, family = "Calibri"),
                       plot.title = element_text(size = 20, family = "Calibri"))
  
  #if (extra == "sw1d" | extra == "sw2d") {
  #  plot <- plot + scale_x_discrete(labels = xlabels)
  #}
  
  plot
}

prob_distribution <- function(mean) {
  library(ggplot2)
  
  plot <- ggplot(data = data.frame(probability = c(0, 1)), aes(probability))
  plot <- plot + stat_function(fun = dnorm, n = 101, args = list(mean = mean, sd = 0.1))
  
  
  plot <- plot + geom_vline(xintercept = 0.0, linetype = "dotted")
  plot <- plot + geom_vline(xintercept = 1.0, linetype = "dotted")
  if (mean == 0.75) {
    
  }
  
  plot <- plot + ylab("")
  plot <- plot + scale_y_continuous(breaks = NULL)
  plot <- plot + theme(panel.grid.major.x = element_blank(),
                       panel.grid.minor.x = element_blank(),
                       text = element_text(size = 20, family = "Linux Biolinum"),
                       axis.text = element_text(family = "Calibri"))
  plot
}

prob_distribution_cairo <- function(mean, name) {
  plot <- prob_distribution(mean)
  cairo(plot, name)
}

cairo <- function(plot, name) {
  library(Cairo)
  
  CairoSVG(paste(name, "svg", sep="."))
  print(plot)
  dev.off()
  
  #CairoPNG(paste(name, "png", sep="."))
  #print(plot)
  #dev.off()
}

seom_plot_all_cairo <- function(csvfile, parameter, title, nominal, extra) {
  library(Cairo)
  library(gridExtra)
  library(berryFunctions)
  
  plot1 <- seom_plot(csvfile, parameter, "stability", title, nominal, extra)
  plot2 <- seom_plot(csvfile, parameter, "morality", title, nominal, extra)
  
  basefilename <- gsub(".csv$", "", csvfile)
  
  cairo(plot1, paste(basefilename, "stability", sep="_"))
  cairo(plot2, paste(basefilename, "morality", sep="_"))
  
  combinedbasefilename <- paste(basefilename, "combined", sep="_")
  
  CairoSVG(paste(combinedbasefilename, "svg", sep="."),
           width = 12, height = 6)
  grid.arrange(plot1, plot2, ncol = 2)
  dev.off()
  
  #CairoPNG(paste(combinedbasefilename, "png", sep="."),
  #         width = 12, height = 6)
  #grid.arrange(plot1, plot2, ncol = 2)
  #dev.off()
  
  openFile(paste(combinedbasefilename, "svg", sep="."))
}

plot_parameters_all <- function(basedir) {
  plot_parameters(basedir, "PrisonersDilemma", "FullyConnected")
  plot_parameters(basedir, "PrisonersDilemma", "Lattice1D")
  plot_parameters(basedir, "PrisonersDilemma", "Lattice2D")
  plot_parameters(basedir, "PrisonersDilemma", "SmallWorld1D")
  plot_parameters(basedir, "PrisonersDilemma", "SmallWorld2D")
  plot_parameters(basedir, "PrisonersDilemma", "BoundedDegree")
  plot_parameters(basedir, "PrisonersDilemma", "FullyRandom")
  
  plot_parameters(basedir, "StagHunt", "FullyConnected")
  plot_parameters(basedir, "StagHunt", "Lattice1D")
  plot_parameters(basedir, "StagHunt", "Lattice2D")
  plot_parameters(basedir, "StagHunt", "SmallWorld1D")
  plot_parameters(basedir, "StagHunt", "SmallWorld2D")
  plot_parameters(basedir, "StagHunt", "BoundedDegree")
  plot_parameters(basedir, "StagHunt", "FullyRandom")
  
  plot_parameters(basedir, "BargainingSubgame", "FullyConnected")
  plot_parameters(basedir, "BargainingSubgame", "Lattice1D")
  plot_parameters(basedir, "BargainingSubgame", "Lattice2D")
  plot_parameters(basedir, "BargainingSubgame", "SmallWorld1D")
  plot_parameters(basedir, "BargainingSubgame", "SmallWorld2D")
  plot_parameters(basedir, "BargainingSubgame", "BoundedDegree")
  plot_parameters(basedir, "BargainingSubgame", "FullyRandom")
  
  plot_parameters(basedir, "UltimatumSubgame", "FullyConnected")
  plot_parameters(basedir, "UltimatumSubgame", "Lattice1D")
  plot_parameters(basedir, "UltimatumSubgame", "Lattice2D")
  plot_parameters(basedir, "UltimatumSubgame", "SmallWorld1D")
  plot_parameters(basedir, "UltimatumSubgame", "SmallWorld2D")
  plot_parameters(basedir, "UltimatumSubgame", "BoundedDegree")
  plot_parameters(basedir, "UltimatumSubgame", "FullyRandom")
}

plot_parameters <- function(basedir, game, network) {
  directory <- paste(basedir, paste(game, network, sep="/"), sep="/")
  
  gametitle <- ""
  if (game == "PrisonersDilemma") {
    gametitle <- "Prisoner's Dilemma"
  } else if (game == "StagHunt") {
    gametitle <- "Stag Hunt"
  } else if (game == "BargainingSubgame") {
    gametitle <- "Bargaining Subgame"
  } else if (game == "UltimatumSubgame") {
    gametitle <- "Ultimatum Subgame"
  } else {
    stop("unknown game")
  }
  
  networktitle <- ""
  if (network == "FullyConnected") {
    networktitle <- "Fully Connected"
  } else if (network == "Lattice1D") {
    networktitle <- "Lattice 1D"
  } else if (network == "Lattice2D") {
    networktitle <- "Lattice 2D"
  } else if (network == "SmallWorld1D") {
    networktitle <- "Small-world 1D"
  } else if (network == "SmallWorld2D") {
    networktitle <- "Small-world 2D"
  } else if (network == "BoundedDegree") {
    networktitle <- "Bounded-degree"
  } else if (network == "FullyRandom") {
    networktitle <- "Fully Random"
  } else {
    stop("unknown network")
  }
  
  title <- paste(gametitle, networktitle, sep="\n")
  
  # NETWORK INDEPENDENT PARAMETERS
  
  file <- paste(directory, "cooperation_incentive.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "cooperation incentive", title, "1", "")
  }
  
  file <- paste(directory, "defection_incentive.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "defection incentive", title, "1", "")
  }
  
  file <- paste(directory, "initial_moral_mean.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "initial moral mean", title, "0.5", "")
  }
  
  file <- paste(directory, "learning_rule.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "learning rule", title, "IBest", "")
  }

  file <- paste(directory, "mutation_distance.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "mutation distance", title, "0", "")
  }
  
  file <- paste(directory, "mutation_probability.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "mutation probability", title, "0", "")
  }
  
  file <- paste(directory, "population_size.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "population size", title, "1000", "")
  }
  
  file <- paste(directory, "risk_dominance.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "risk dominance", title, "true", "")
  }
  
  # NETWORK DEPENDENT PARAMETERS
  
  file <- paste(directory, "learning_distance.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "learning distance", title, "1", "")
  }
  
  file <- paste(directory, "wrap_around.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "wrap around", title, "true", "")
  }
  
  file <- paste(directory, "degree_interval.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "degree interval", title, "4-7", "")
  }
  
  file <- paste(directory, "edge_probability.csv", sep="/")
  if (file.exists(file)) {
    seom_plot_all_cairo(file, "edge probability", title, "0.008", "")
  }
  
  # SPECIAL: NEIGHBORHOOD
  
  file <- paste(directory, "neighborhood.csv", sep="/")
  if (file.exists(file)) {
    if (network == "Lattice1D" | network == "SmallWorld1D") {
      seom_plot_all_cairo(file, "neighborhood", title, "2", "")
    } else if (network == "Lattice2D" | network == "SmallWorld2D") {
      seom_plot_all_cairo(file, "neighborhood", title, "M8", "2d")
    } else {
      stop("error")
    }
  }
  
  
  # SPECIAL: BETA
  
  file <- paste(directory, "beta.csv", sep="/")
  if (file.exists(file)) {
    if (network == "SmallWorld1D") {
      seom_plot_all_cairo(file, "beta", title, "0.125", "")
    } else if (network == "SmallWorld2D") {
      seom_plot_all_cairo(file, "beta", title, "0.09", "")
    } else {
      stop("error")
    }
  }
}
