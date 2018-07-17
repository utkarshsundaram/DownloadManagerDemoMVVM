# DownloadManagerDemoMVVM
The download manager is a system service that handles long-running HTTP downloads.
The download manager will conduct the download in the background, taking care of HTTP interactions and retrying downloads after failures or across connectivity changes and system reboots.

# Limitations
After the connection is lost while downloading a particular file the download manager give 2.5 min window where it retries to find the network after 
every 30 sec for 5 times.After which if it dosen't find the network it will remove the downloaded files cache i.e resumable downloading after 2.5 min is not possible while using this
