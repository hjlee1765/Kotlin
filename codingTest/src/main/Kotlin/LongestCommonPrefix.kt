fun main(){
    var solution = LongestCommonPrefix()
    print(solution.longestCommonPrefix(arrayOf("a", "aav")))
}

class LongestCommonPrefix {
    fun longestCommonPrefix(strs: Array<String>): String {
        var res = ""
        if(!strs.isNullOrEmpty()){
            var firstWord = strs[0]
            for(fwIndex in firstWord.indices){
                for(index in strs.indices){
                    if(strs[index].length <= fwIndex || firstWord[fwIndex] != strs[index][fwIndex]){
                        return res
                    }
                }
                res += firstWord[fwIndex]
            }
        }
        return res
    }
}