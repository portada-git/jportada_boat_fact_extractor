@echo off
Setlocal EnableDelayedExpansion
set command=-jar boat_fact_extractor.jar
for %%a in (%*) do set command=!command! %%a
java %command%