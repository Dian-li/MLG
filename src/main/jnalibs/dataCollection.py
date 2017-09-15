# -*- coding:utf-8 -*-
import pexpect
import re
import time
import threading
import sys
import signal
import os
import string
import redis
from pexpect import pxssh

r = redis.Redis(host='localhost',port=6379,db=0)


class Collection:
    global STATUS

    def __init__(self, status):
        self.STATUS = status

    # 主方法
    def ssh_command(self, user, host, password):
        try:
            s = pxssh.pxssh()
            s.login(host, user, password)
        except pxssh.ExceptionPxssh as e:
            print("pxssh failed on login.")
            print e
        return s

    # mem
    def mem_info(self, n):
        with open('data/mem.txt', 'a') as f:
            s = self.ssh_command("root", "10.108.211.214", "cintel")
            f.write("date,time,mem,vmem\n")
            while self.STATUS:
                print 'mem_info'
                s.sendline('cat /proc/meminfo')
                s.prompt()
                #print 'meminfloop'
                nowdate = time.strftime("%Y-%m-%d", time.localtime())
                f.write(time.strftime("%Y-%m-%d,", time.localtime()))
                nowtime = time.strftime("%H:%M:%S", time.localtime())
                f.write(time.strftime("%H:%M:%S,", time.localtime()))
                mem = s.before
                #print 'mem:' + str(mem)
                mem_values = re.findall("(\d+)\ kB", mem)
                MemTotal = mem_values[0]
                MemFree = mem_values[1]
                Buffers = mem_values[2]
                Cached = mem_values[3]
                SwapTotal = mem_values[13]
                SwapFree = mem_values[14]
                VmTotal = mem_values[22]
                VmUsed = mem_values[23]
                # if int(SwapTotal) == 0:
                #     f.write("0,")
                # else:
                #     Rate_Swap = 100 - 100 * int(SwapFree) / float(SwapTotal)
                #     f.write(str("%.4f" % Rate_Swap) + ",")
                Free_Mem = int(MemFree) + int(Buffers) + int(Cached)
                Used_Mem = int(MemTotal) - Free_Mem
                Rate_Mem = 100 * Used_Mem / float(MemTotal)
                Rate_Vm = 100 * float(VmUsed)/float(VmTotal)
                f.write(str("%.4f," % Rate_Mem))
                f.write(str("%.4f" % Rate_Vm))
                f.write("\n")
                r.hset("MEM","date",nowdate)
                r.hset("MEM","time",nowtime)
                r.hset("MEM","mem",str("%.4f," % Rate_Mem))
                r.hset("MEM","vmem",str("%.4f," % Rate_Vm))
                time.sleep(n)
    #get the mem and vmem of the progress
    def mem_data(self,s):
        s.sendline('cat /proc/meminfo')
        s.prompt()
        mem = s.before
        mem_values = re.findall("(\d+)\ kB", mem)
        MemTotal = mem_values[0]
        MemFree = mem_values[1]
        Buffers = mem_values[2]
        Cached = mem_values[3]
        SwapTotal = mem_values[13]
        SwapFree = mem_values[14]
        VmTotal = mem_values[22]
        VmUsed = mem_values[23]
        return MemTotal,VmTotal
    #get pid mem/vmem
    def progress_mem_data(self,pidlist,s):
        pidmem = []
        pidvmem = []
        for pid in pidlist:
            cmd_pidmem = "cat /proc/" + pid + "/status | grep 'VmRSS'"#get the mem
            s.sendline(cmd_pidmem)
            s.prompt()
            pidmemlist = re.split('\s+', s.before.replace(cmd_pidmem, '').strip())
            pidmem.append(string.atof(pidmemlist[1]))
            cmd_pidvmem = "cat /proc/" + pid + "/status | grep 'VmData'"  # get the mem
            s.sendline(cmd_pidvmem)
            s.prompt()
            pidvmemlist = re.split('\s+', s.before.replace(cmd_pidvmem, '').strip())
            pidvmem.append(string.atof(pidvmemlist[1]))
        return pidmem,pidvmem
    def progress_mem_info(self,progress,n):
        s = self.ssh_command("root", "10.108.211.214", "cintel")
        getpid = "ps -def | grep " + progress + " | grep -v grep | awk '{ print $2 }' "
        s.sendline(getpid)
        s.prompt()
        pidlist = s.before.strip().split('\r\n')[1:]
        #print pidlist
        progressname = progress.strip().split('/')[-1]
        #print progressname
        with open("data/mem_" + progressname + ".txt", 'a') as f:
            f.write("date,time,mem,min_mem,max_mem,aver_mem,vmem,min_vmem,max_vmem,avermem\n")
            if len(pidlist) < 1: return
            while self.STATUS:
                print "pmem_info"
                mem1,vmem1 = self.mem_data(s)
                pidmem1,pidvmem1 = self.progress_mem_data(pidlist, s)
                #mem
                summem = float(mem1)
                sumpmem = 100 * string.atof(sum(pidmem1)) / summem
                minppmem = 100 * string.atof(min(pidmem1)) / summem
                maxpmem = 100 * string.atof(max(pidmem1)) / summem
                averpmem = sumpmem / len(pidmem1)
                f.write(time.strftime("%Y-%m-%d,", time.localtime()))
                f.write(time.strftime("%H:%M:%S,", time.localtime()))
                f.write(str('%.4f,' % (sumpmem)))
                f.write(str('%.4f,' % (minppmem)))
                f.write(str('%.4f,' % (maxpmem)))
                f.write(str('%.4f,' % (averpmem)))
                #vmem
                sumvmem = string.atof(vmem1)
                sumpvmem = 100 * string.atof(sum(pidvmem1)) / sumvmem
                minppvmem = 100 * string.atof(min(pidvmem1))/ sumvmem
                maxpvmem = 100 * string.atof(max(pidvmem1)) / sumvmem
                averpvmem = sumpvmem / len(pidvmem1)
                f.write(str('%.4f,' % (sumpvmem)))
                f.write(str('%.4f,' % (minppvmem)))
                f.write(str('%.4f,' % (maxpvmem)))
                f.write(str('%.4f' % (averpvmem)))
                f.write("\n")
                time.sleep(n)

    # io读写速度kb/s
    def io_info(self, n):
        with open('data/io.txt', 'a') as f:
            s = self.ssh_command("root", "10.108.211.214", "cintel")
            print 'io'
            s.sendline('cat /proc/diskstats | grep "sda"')
            s.prompt()
            #print 'ioheader'
            f.write('date,time,')
            io_info_header = self.io_header(s.before)
            for i in range(0, len(io_info_header)):
                if (i == len(io_info_header) - 1):
                    f.write(io_info_header[i] + "_read,")
                    f.write(io_info_header[i] + "_write")
                else:
                    f.write(io_info_header[i] + "_read,")
                    f.write(io_info_header[i] + "_write,")
            f.write('\n')
            while self.STATUS:
                print "io_info"
                nowdate = time.strftime("%Y-%m-%d", time.localtime())
                f.write( time.strftime("%Y-%m-%d,", time.localtime()))
                r.hset("IO","date",nowdate)
                nowtime = time.strftime("%H:%M:%S", time.localtime())
                f.write(time.strftime("%H:%M:%S,", time.localtime()))
                r.hset("IO","time",nowtime)
                #print 'ioloop'
                for i in range(0, len(io_info_header)):
                    str2 = 'iostat -d ' +str(io_info_header[i])+ ' -k '+str(n)+' 1 | grep "sda"'
                    s.sendline(str2)
                    s.prompt()
                    io_first = self.io_data(s.before)
                    for j in range(0, len(io_first)):
                        if i == (len(io_info_header)-1) and j==len(io_first)-1:
                            f.write(str(io_first[j]))
                            r.hset("IO",io_info_header[i]+"_write",str(io_first[j]))
                        else:
                            f.write(str(io_first[j])+ ",")
                            if j%2==0:
                                r.hset("IO", io_info_header[i] + "_read", str(io_first[j]))
                            else:
                                r.hset("IO", io_info_header[i] + "_write", str(io_first[j]))
                f.write("\n")
                time.sleep(n)
                # io表头

    def io_header(self, str):
        newstr = str.strip().split('\r\n')
        result = []

        for i in range(1, len(newstr)):
            list = re.split('\s+', newstr[i].strip())
            result.append(list[2])
        return result

    # io数据
    def io_data(self, str):
        newstr = str.strip().split('\r\n')
        result = []
        data1 = re.split('\s+', newstr[1].strip())
        result.append(data1[2])
        result.append(data1[3])
        return result
    #cpu data,first index is 1
    def cpu_data(self,s):
        shell_cmd = "cat /proc/stat | grep 'cpu '"
        s.sendline(shell_cmd)
        s.prompt()
        newstr = s.before.replace(shell_cmd, '')
        #print 'before: ' + newstr
        cpustat_info = newstr.strip().split()
        idel = string.atof(cpustat_info[4])
        user = string.atof(cpustat_info[1])
        cpu = string.atof(cpustat_info[1]) + string.atof(cpustat_info[2]) + string.atof(
            cpustat_info[3]) + string.atof(cpustat_info[4]) + string.atof(cpustat_info[5]) + string.atof(
            cpustat_info[6]) + string.atof(cpustat_info[7]) + string.atof(cpustat_info[8]) + string.atof(
            cpustat_info[9])
        return idel,cpu
    # cpu,exec cmd and get user and sum cpu
    def cpu_info(self, n):
        with open('data/cpu.txt', 'a') as f:
            s = self.ssh_command("root", "10.108.211.214", "cintel")
            f.write("date,time,user_cpu\n")
            while self.STATUS:
                print 'cpu_info'
                idel1,cpu1 = self.cpu_data(s)
                time.sleep(n)
                idel2,cpu2 = self.cpu_data(s)
                nowdate = time.strftime("%Y-%m-%d", time.localtime())
                f.write(time.strftime("%Y-%m-%d,", time.localtime()))
                r.hset("CPU","date",nowdate)
                nowtime = time.strftime("%H:%M:%S", time.localtime())
                f.write(time.strftime("%H:%M:%S,", time.localtime()))
                r.hset("CPU","time",nowtime)
                cpu = 100*(1-((idel2 - idel1) / (cpu2 - cpu1)))
                r.hset("CPU","cpu",str('%.4f' %cpu ))
                f.write(str('%.4f' %cpu )+ "\n")


    #get the cpu info on one progress
    def progress_cpu_info(self,progress,n):
        s = self.ssh_command("root", "10.108.211.214", "cintel")
        getpid = "ps -def | grep "+ progress +"|grep -v grep | awk '{ print $2 }' "
        s.sendline(getpid)
        s.prompt()
        pidlist = s.before.strip().split('\r\n')[1:]
        #print pidlist
        progressname = progress.strip().split('/')[-1]
        #print progressname
        with open("data/cpu_"+progressname+".txt", 'a') as f:
            f.write("date,time,cpu,min_cpu,max_cpu,aver_cpu\n")
            if len(pidlist)<1:return
            while self.STATUS:
                print "pcpu_info"
                user1,cpu1 = self.cpu_data(s)
                progress1 = self.progress_cpu_data(pidlist,s)
                time.sleep(n)
                user2, cpu2 = self.cpu_data(s)
                progress2 = self.progress_cpu_data(pidlist, s)
                sumcpu = string.atof(cpu2-cpu1)
                sumpcpu = 100*string.atof(sum(progress2)-sum(progress1))/sumcpu
                minpcpu = 100*string.atof(min([progress2[i]-progress1[i] for i in range(len(progress2))]))/sumcpu
                maxpcpu = 100*string.atof(max([progress2[i]-progress1[i] for i in range(len(progress2))]))/sumcpu
                averpsum = 100*sumpcpu/len(progress1)
                f.write(time.strftime("%Y-%m-%d,", time.localtime()))
                f.write(time.strftime("%H:%M:%S,", time.localtime()))
                f.write(str('%.4f,' % (sumpcpu)))
                f.write(str('%.4f,' % (minpcpu)))
                f.write(str('%.4f,' % (maxpcpu)))
                f.write(str('%.4f' % (averpsum)))
                f.write("\n")

    #get /proc/pid/stat get the cpu time on user and system,return a list
    def progress_cpu_data(self,pidlist,s):
        cpusum = []
        for pid in pidlist:
            cmd_pidcpu = "cat /proc/" + pid + "/stat"
            s.sendline(cmd_pidcpu)
            s.prompt()
            pidcpulist = re.split('\s+', s.before.replace(cmd_pidcpu, '').strip())
            cpusum.append(string.atof(pidcpulist[13]) + string.atof(pidcpulist[14]))
        return cpusum
    # net,网络带宽，bytes/sec
    def net_info(self, n):
        with open('data/net.txt', 'a') as f:
            s = self.ssh_command("root", "10.108.211.214", "cintel")
            shell_cmd = "cat /proc/net/dev"
            s.sendline(shell_cmd)
            s.prompt()
            title_str = self.net_strhandle(s.before)
            f.write('date,time,')
            for i in range(0, len(title_str)):
                if (i == len(title_str) - 1):
                    f.write(title_str[i] + "_recv,")
                    f.write(title_str[i] + "_send")
                else:
                    f.write(title_str[i] + "_recv,")
                    f.write(title_str[i] + "_send,")
            f.write("\n")
            while self.STATUS:
                print "net_info"
                shell_cmd = "cat /proc/net/dev"
                s.sendline(shell_cmd)
                s.prompt()
                netlist = self.net_datahandle(s.before)
                time.sleep(n)
                shell_cmd = "cat /proc/net/dev"
                s.sendline(shell_cmd)
                s.prompt()
                netlist2 = self.net_datahandle(s.before)
                netresult = [(string.atof(netlist2[i]) - string.atof(netlist[i])) for i in range(0, len(netlist))]

                nowdate = time.strftime("%Y-%m-%d", time.localtime())
                f.write( time.strftime("%Y-%m-%d,", time.localtime()))
                r.hset("NET","date",nowdate)
                nowtime = time.strftime("%H:%M:%S", time.localtime())
                f.write(time.strftime("%H:%M:%S,", time.localtime()))
                r.hset("NET","time",nowtime)
                j=0
                for i in range(0, len(netresult)):
                    if (i == len(netresult) - 1):
                        f.write(str('%.4f' % (netresult[i] / n)))
                        r.hset("NET",title_str[j]+"_send",str('%.4f' % (netresult[i] / n)))
                    else:
                        f.write(str('%.4f' % (netresult[i] / n)) + ",")
                        if i%2==0:
                            r.hset("NET",title_str[j]+"_recv",str('%.4f' % (netresult[i] / n)))
                        else:
                            r.hset("NET", title_str[j] + "_send", str('%.4f' % (netresult[i] / n)))
                    if i%2!=0:
                        j = j+1
                f.write("\n")

    # 网络表头
    def net_strhandle(self, str):
        newstr = str.strip().split('\r\n')
        result = []
        for i in range(3, len(newstr)):
            result.append(newstr[i].strip().split(':')[0])
        return result

    # 网络数据
    def net_datahandle(self, str):
        newstr = str.strip().split('\r\n')
        result = []
        for i in range(3, len(newstr)):
            tmp = newstr[i].strip().split(':')[1].strip().split()
            result.append(tmp[0])
            result.append(tmp[8])
        return result

    # 删除文件
    def delDir(self, dir):
        for root, dirs, files in os.walk(dir, topdown=False):
            for name in files:
                os.remove(os.path.join(root, name))
            for name in dirs:
                os.rmdir(os.path.join(root, name))
        os.rmdir(dir)


def quit(signum, frame):
    print 'You choose to stop me.'
    Collection.STATUS = False
    sys.exit()
def stop():
    print "stop"
def start():
    try:
        reload(sys)
        sys.setdefaultencoding('utf-8')
        collection = Collection(True)
        signal.signal(signal.SIGINT, quit)
        signal.signal(signal.SIGTERM, quit)
        r.delete("IO")
        r.delete("MEM")
        r.delete("CPU")
        r.delete("NET")
        dir = 'data'
        if not os.path.isdir(dir):
            os.makedirs(dir)
        else:
            collection.delDir(dir)
            os.makedirs(dir)
        threads = []
        t1 = threading.Thread(target=collection.mem_info, args=(2.0,))
        threads.append(t1)
        t2 = threading.Thread(target=collection.io_info, args=(2.0,))
        threads.append(t2)
        t3 = threading.Thread(target=collection.cpu_info,args=(2.0,))
        threads.append(t3)
        t4 = threading.Thread(target=collection.net_info, args=(2.0,))
        threads.append(t4)
        t5 = threading.Thread(target=collection.progress_cpu_info, args=("/usr/libexec/gvfsd-metadata",2.0))
        threads.append(t5)
        t6 = threading.Thread(target=collection.progress_mem_info, args=("/usr/libexec/gvfsd-metadata", 2.0))
        threads.append(t6)
        for n in range(len(threads)):
            threads[n].start()
    except Exception, e:
        print str(e)
if __name__ == '__main__':
    try:
        reload(sys)
        sys.setdefaultencoding('utf-8')
        collection = Collection(True)
        signal.signal(signal.SIGINT, quit)
        signal.signal(signal.SIGTERM, quit)
        r.delete("IO")
        r.delete("MEM")
        r.delete("CPU")
        r.delete("NET")
        dir = 'data'
        if not os.path.isdir(dir):
            os.makedirs(dir)
        else:
            collection.delDir(dir)
            os.makedirs(dir)
        threads = []
        t1 = threading.Thread(target=collection.mem_info, args=(2.0,))
        threads.append(t1)
        t2 = threading.Thread(target=collection.io_info, args=(2.0,))
        threads.append(t2)
        t3 = threading.Thread(target=collection.cpu_info,args=(2.0,))
        threads.append(t3)
        t4 = threading.Thread(target=collection.net_info, args=(2.0,))
        threads.append(t4)
        t5 = threading.Thread(target=collection.progress_cpu_info, args=("/usr/libexec/gvfsd-metadata",2.0))
        threads.append(t5)
        t6 = threading.Thread(target=collection.progress_mem_info, args=("/usr/libexec/gvfsd-metadata", 2.0))
        threads.append(t6)
        for n in range(len(threads)):
            threads[n].start()
    except Exception, e:
        print str(e)
