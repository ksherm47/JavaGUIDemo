// ".'--'." -NS
//
//This program will allow for userUserInput for the SPI connection and
//can be used to test specific values

#include <iostream>
#include <errno.h>
#include <wiringPiSPI.h>
#include <unistd.h>
#include <bitset>
#include <idConfig.h>

using namespace std;

static const int CHANNEL = 0;

int main(int argc, char** argv)
{
  int SPIInput, UserInput, result;
  unsigned char ResistorBuffer[100];

  //Interface config
  //wiringPiSPISetup(CHANNEL, Bus_Speed)
  SPIInput = wiringPiSPISetup(CHANNEL, 780800);
  
  ResistorBuffer[0] = 0;
  ResistorBuffer[1] = 0;
  wiringPiSPIDataRW(CHANNEL, ResistorBuffer, 1);
  UserInput = 0x00;

  sleep(3);

  while(UserInput != 0xFF)
  {
    //cin >> hex >>UserInput;
    
	if(UserInput != 0xFF)
    {
      //Byte 1 ofUserInput is stored in ResistorBuffer[0]
      //  This ranges from 0-3, 0 enables A, 1 enables B, 2 enables C, and
      //  3 enables all phases
      //ResistorBuffer[0] =UserInput;    //  /16;
	  
	  ResistorBuffer[0] = argv[0];

      //Byte 2 ofUserInput is stored in ResistorBuffer[1]
      //  This ranges from 0-15 (0-F), where 0 enables 1 resistor, and
      //  F anables all resistors in the enabled phases
      //ResistorBuffer[1] =UserInput%16;
	  
	  ResistorBuffer[1] = argv[1];


      //cout << "Writing    " << bitset<32>(ResistorBuffer[0]) << " to the array" << endl;
      //cout << "Following  " << bitset<32>(ResistorBuffer[1]) << " to the array" << endl;


      //Writes the ResistorBuffer as a whole to the array, sending both bytes
      result = wiringPiSPIDataRW(CHANNEL, ResistorBuffer, 1);
      //cout << result << endl;
    }
      sleep(5);
  }

  //cout << "Resetting array to default 0x00" << endl;
  ResistorBuffer[0] = 0;
  ResistorBuffer[1] = 0;
  wiringPiSPIDataRW(CHANNEL, ResistorBuffer, 1);
  sleep(2);

}
