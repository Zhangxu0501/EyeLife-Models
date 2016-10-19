package start_record;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import baidu_voice_to_characters.getWords;

public class testRecord
{


	public static void main(String[] args) throws Exception
	{
			testRecord mr =new testRecord();
			mr.capture();
			System.out.println("end recording...");
			mr.stopflag = true;
			mr.save("zhangxu.mp3");

		
	}

	

	
	AudioFormat af = null;
	
	TargetDataLine td = null;
	SourceDataLine sd = null;
	ByteArrayInputStream bais = null;
	ByteArrayOutputStream baos = null;
	AudioInputStream ais = null;
	static Boolean stopflag = false;

	public void capture() throws Exception
	{
		try
		{
			
			af = null;
			af = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
			td = (TargetDataLine) (AudioSystem.getLine(info));
			
			td.open(af);
			
			td.start();

			
			Record record = new Record();
			Thread t1 = new Thread(record);
			t1.start();

			Thread.sleep(2000);

			td.close();
			

		}
		catch (LineUnavailableException ex)
		{
			ex.printStackTrace();
			return;
		}
	}

	
	public void stop()
	{
		stopflag = true;
	}

	public void save(String files)
	{
		
		af = getAudioFormat();

		byte audioData[] = baos.toByteArray();
		bais = new ByteArrayInputStream(audioData);
		ais = new AudioInputStream(bais, af, audioData.length / af.getFrameSize());
		
		File file = null;
		
		try
		{
			
			file = new File(files);
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			
			try
			{
				if (bais != null)
				{
					bais.close();
				}
				if (ais != null)
				{
					ais.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
	public AudioFormat getAudioFormat()
	{
		
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float rate = 8000f;
		int sampleSize = 16;
		String signedString = "signed";
		boolean bigEndian = true;
		int channels = 1;
		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
	
	}

	
	class Record implements Runnable
	{
		
		byte bts[] = new byte[10000];

	
		public void run()
		{
			baos = new ByteArrayOutputStream();
			try
			{
				stopflag = false;
				while (true)
				{
					if (stopflag == true)
					{
						td.flush();
						td.stop();
						break;

					}

				
					int cnt = td.read(bts, 0, bts.length);
					if (cnt > 0)
					{
						baos.write(bts, 0, cnt);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					
					if (baos != null)
					{
						baos.close();
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					td.drain();

				}
			}
		}

	}

	
	class Play implements Runnable
	{
		
		public void run()
		{
			byte bts[] = new byte[1000];
			try
			{
				int cnt;
				
				while ((cnt = ais.read(bts, 0, bts.length)) != -1)
				{
					if (cnt > 0)
					{
						
						sd.write(bts, 0, cnt);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				sd.drain();
				sd.close();
			}
		}
	}
}