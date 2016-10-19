#coding:utf-8
import mp3play,time
clip = mp3play.load("output.wav")
clip.play()
time.sleep(10)   #定义播放时间，如果没有这句话，是听不到声音的。
clip.stop()