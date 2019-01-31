import os

class Templater:

    def __init__(self, template_dir, snippet_dir):
        self.template_dir = template_dir
        self.snippet_dir = snippet_dir

    def render(self, template_name):
        template = open(os.path.join(self.template_dir, template_name), 'r')

        str_temp = ""
        for line in template:
            str_temp += line

        str_result = ""

        state = 0
        snippet_len = 0
        snippet = ""
        for i in range(len(str_temp)):
            char = str_temp[i]
            str_result += char

            if char == '[' and state == 0: 
                state = 1
                snippet_len += 1
            if char == '!' and state == 1:
                state = 2
                snippet_len += 1
                continue
            if char == ']' and state == 2:
                str_result = str_result[:i - snippet_len] #cuts off the snippet code
                str_result += self.get_snippet(snippet) #adds the snippet's content in
                
                #reset
                state = 0
                snippet_len = 0
                snippet = ""
                continue

            if state == 2:
                snippet += char
                snippet_len += 1

        return str_result

    def get_snippet(self, snippet_name):
        snippet = open(os.path.join(self.snippet_dir, snippet_name + ".snip"), 'r')

        as_str = ""
        for line in snippet:
            as_str += line
        
        return as_str

