from tkinter import *
import hog

def plot(strategy, scale = 4):
  master = Tk()

  w = Canvas(master, width = scale*102, height=scale*102)
  w.pack()

  for home_score in range(100):
    for away_score in range(100):
      value = strategy(home_score, away_score)
      color = "gray" + str(100 - value * 10)
      if value == 0:
        color = "LightBlue"
      elif value == -1:
        color = "red"
      topleft_x = away_score * scale + scale
      topleft_y = home_score * scale + scale
      w.create_rectangle(topleft_x, topleft_y, topleft_x + scale, topleft_y + scale, fill = color)

  master.resizable(0, 0)
  mainloop()

if __name__ == "__main__":
  plot(hog.final_strategy, scale = 4) # Replace this with your own strategy call and the scaling factor