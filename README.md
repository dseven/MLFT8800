# MLFT8800 - A Java front-panel for the Yaesu FT-8800R (and maybe FT-8900R too?)

WIP! I wanted to share this in case it's useful to someone else, but need to finish documenting, etc....

## Motivation

I found myself with two working FT-8800R base units, and zero working control heads (for different reasons). Since this is my favourite dual-band mobile, I really wanted to get at least one of them working again, but I wanted to be sure that the base units were really still good before ordering a new control head. This provided some motivation to look for a cheap solution.

## Inspiration

Some Googling found a document that had originally been made available by "Interlock Rochester", which describes how the control head communicates with the base unit over a serial connection, and details the protocol. This document was vital in getting my implementation working, although I did find a few errors in it (specifically some of the display elements).

## Communication

For serial communication, I used a 3.3V TTL-to-USB cable named "EZSync012" from Purenitetech LLC [link](https://purenitetech.com/product/ezsync-ftdi-chip-usb-to-ttl-serial-cable-for-rapsberry-pi-3-3v/), which I already had at hand (from some Arduino-related project). It connects directly to the RX, TX and ground pins of the modular connector. I just a push button and a 1k resistor to cobble together a power button. It I was making a permanent solution, I might look into using a transitor on the RTS or DTR line of a serial cable that includes those (maybe a EZSync007). Code would need to be added to implement a software power-on button to drive that.

## Transmit (mic) audio

I use my FT-8800R(s) almost entirely for listening these days. I have not attempted to get transmit audio working. The real front panel includes a mic pre-amp on a small PCB refered to as the "Panel sub-unit" in the service manual. Something like that would need to be implemented for TX with a stock mic to work.

## UI

The UI is an attempt to approximate the physical front-panel. It's a bit hokey in some ways, and I'm sure I missed some elements, but it basically works pretty well, for me, at least.

The buttons in the upper corners have the effect of pushing the select knobs, and the +/- buttons in the lower corners have the effect of turning those knobs. 

## Development

The NetBeans IDE was used for development.

Serial communication uses the awesome [PureJavaComm](http://www.sparetimelabs.com/purejavacomm/purejavacomm.php) - many thanks to Kusti for making that available!
