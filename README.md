# Custom Plugin

> This repository is one part of my work in GSOC'18, we have developed two filters Lagendre Filter and Texture Descriptor Based filters and integrated Zernike Filter (from previous year work) that have been added in the Active Segmentation toolbox.


### Filters on GUI

<img src="https://user-images.githubusercontent.com/20872683/43735808-39118f96-99d9-11e8-8904-edaeb2be798f.PNG" align="center" />


### Run & Use the toolbox

Now these filters have been added to the Active Segmentation toolbox itself through addition of source code. To run this project follow these easy steps :

1. First of all install Active Segmentation and imageJ, follow [this](https://sumit3203.github.io/install.html)
2. Now you have ImageJ and Active Segmentation installed.
3. To perform Classification :  Follow this [tutorial](https://getsanjeev.github.io/demo_gsoc.html).
4. To perform Segmentation : Follow this [tutorial](https://sumit3203.github.io/userguide.html).


### Development

1. The so far developed customPlugin should serve as an example for addition of new filter.
2. Make changes in the existing source (modify/add filter classes or filter_core classes) and Build the project.
3. Go to File>Export>As-runnable-Jar>any-option>OK, now the jar of customplugin has been created.
5. Place the jar in the imageJ plugins folders.

Note: Currently we have two versions of filters with different return encodings in the repo, To make the jar one needs to remove one of them. Select according to what return type FilterManager expects. During the development of this plugin it was v1, testcodes are loosely based on v1.
During adding it to ActiveSegmentation toolbox we changed the encoding as per in filterv2, so have put it here. Next development should be based on v2.


#### Prerequisites

The following must be installed and configured:
* Java
* ImageJ