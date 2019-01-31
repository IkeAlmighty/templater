import temppy

templater = temppy.Templater("test/templates/", "test/snippets/")

print(templater.render("index.html"))