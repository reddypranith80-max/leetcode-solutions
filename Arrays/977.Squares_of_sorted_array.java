class Solution {
    public int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int newarr[] = new int[n];
        for(int i=0;i<n;i++){
            newarr[i] = nums[i] * nums[i];
        }
        Arrays.sort(newarr);
        return newarr;
    }
}

// using two pointer approach

class Solution {
    public int[] sortedSquares(int[] nums) {
        int[] res = new int[nums.length];
        int left = 0;
        int right = nums.length - 1;

        for (int i = nums.length - 1; i >= 0; i--) {
            if (Math.abs(nums[left]) > Math.abs(nums[right])) {
                res[i] = nums[left] * nums[left];
                left++;
            } else {
                res[i] = nums[right] * nums[right];
                right--;
            }
        }
        return res;        
    }
}


// using the two pointer approach with a while loop using merge two sorted arrays approach

class Solution {
    public int[] sortedSquares(int[] nums) {
        List<Integer>neg = new ArrayList<>();
        List<Integer>pos = new ArrayList<>();

        int n = nums.length;
        // using two list to add neg in one list and pos in one list 

        for(int num : nums){
            if(num < 0){
                neg.add(num);
            }else{
                pos.add(num);
            }
        }
        // multiply neg arr and reverse sort it 

        for(int i=0;i<neg.size();i++){
            neg.set(i,neg.get(i) * neg.get(i) );
        }
        Collections.reverse(neg);
        // multiply thr pos array auto sorted

        for(int i=0;i<pos.size();i++){
            pos.set(i,pos.get(i) * pos.get(i) );
        }


        if(neg.size() == 0) { // no negative element only pos 
            int ans[] = new int[pos.size()];
            for(int i=0;i<pos.size();i++){
                ans[i] = pos.get(i);
            }
            return ans;
        }

        if(pos.size() == 0){
            int ans[] = new int[neg.size()];
            for(int i=0;i<neg.size();i++){
                ans[i] = neg.get(i);
            }
            return ans;
        }

        int i=0;
        int j=0;
        int idx=0;
        // using two pointer sorted arrays logic 
        int res[] = new int[pos.size() + neg.size()];
        while(i<neg.size() && j<pos.size()){
            if(neg.get(i) <= pos.get(j)){
                res[idx++] = neg.get(i++);
            }else{
                res[idx++] = pos.get(j++);
            }
        }
        while(i < neg.size()){
               res[idx++] = neg.get(i++);
        }

        while(j < pos.size()){
            res[idx++] = pos.get(j++);
        }
        return res;
    }
}