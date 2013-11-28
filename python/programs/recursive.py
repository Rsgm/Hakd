from org.python.util import PythonInterpreter

print recurse

pi = PythonInterpreter()

pi.set("terminal", terminal)
pi.set("recurse", recurse + 1)
pi.execfile('/media/truecrypt1/code/java/Hakd/python/programs/recursive.py')
